package e3i2.ecommerce_backoffice.common.auth.service;

import e3i2.ecommerce_backoffice.common.auth.dto.LoginRequest_NEW;
import e3i2.ecommerce_backoffice.common.auth.dto.LoginResponse_NEW;
import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.jwt.JwtUtil;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;
import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_IN_ACT_ADMIN_ACCOUNT_LOGIN;
import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_SUSPEND_ADMIN_ACCOUNT_LOGIN;
import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_UNAUTHORIZED_ACCOUNT_LOGIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //TODO JWT 테스트
    @Transactional
    public LoginResponse_NEW login_new(LoginRequest_NEW request, HttpServletResponse response) {
        Admin admin = adminRepository.findByEmail(request.email()).orElseThrow(
                () -> new ServiceErrorException(ERR_WRONG_EMAIL_PASSWORD)
        );

        boolean matches = passwordEncoder.matches(
                request.password(), admin.getPassword()
        );

        if (!matches) {
            throw new ServiceErrorException(ERR_WRONG_EMAIL_PASSWORD);
        }

        switch (admin.getStatus()) {
            case WAIT:
                throw new ServiceErrorException(ERR_WAIT_ADMIN_ACCOUNT_LOGIN);
            case DENY:
                throw new ServiceErrorException(ERR_DENY_ADMIN_ACCOUNT_LOGIN);
            case SUSPEND:
                throw new ServiceErrorException(ERR_SUSPEND_ADMIN_ACCOUNT_LOGIN);
            case IN_ACT:
                throw new ServiceErrorException(ERR_IN_ACT_ADMIN_ACCOUNT_LOGIN);
            case ACT:
                break;
            default:
                throw new ServiceErrorException(ERR_UNAUTHORIZED_ACCOUNT_LOGIN);
        }

        log.debug("email value check : {}", request.email());

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(admin.getEmail());


        // 생성한 토큰을 클라이언트 쿠키에 등록하여 활용하게하는 케이스 작성
        //FIXME CSRF 공격 방어는 학습 이후에 다시 세팅 해보기

        // 최신 버전의 경우엔 ResponseCookie 가 인코딩 처리도 해주므로 사용하는게 좋을 듯
        /*
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        */

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // JS 접근 차단
        cookie.setSecure(false); // Secure 보안 여부 (지금은 테스트니 false)
        cookie.setPath("/"); // 모든 path 에서 쿠키 사용
        cookie.setMaxAge(3600); // 1시간 만료시간 설정

        response.addCookie(cookie);
        // 생성한 토큰을 클라이언트 쿠키에 등록하여 활용하게하는 케이스 작성 end

        return new LoginResponse_NEW (
                token //TODO 일단은 반환해서 Authorization 에 담아서 쓰자 (Filter 코드를 보면 암)
        );
    }
}
