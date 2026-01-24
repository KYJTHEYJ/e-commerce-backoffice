package e3i2.ecommerce_backoffice.common.auth.controller;

import e3i2.ecommerce_backoffice.common.auth.dto.LoginRequest_NEW;
import e3i2.ecommerce_backoffice.common.auth.dto.LoginResponse_NEW;
import e3i2.ecommerce_backoffice.common.auth.service.AuthService;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    //TODO JWT 테스트
    @PostMapping("/login_NEW")
    public ResponseEntity<DataResponse<LoginResponse_NEW>> login_new(
            @Valid @RequestBody LoginRequest_NEW request
            , HttpServletResponse response
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(),  authService.login_new(request, response)));
    }

    //TODO JWT 테스트
    @GetMapping("/validate_NEW")
    public ResponseEntity<DataResponse<Boolean>> validate_new(
            HttpServletRequest request
    ) {

        // Bearer 인증 값을 제외한 나머지 값인 토큰 (직접 검증용)
        String token = request.getHeader("Authorization").substring("Bearer ".length());

        /*
        // TODO 후에 refresh Token 을 갖고 쿠키 등록해서 활용해보기
        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();  // 쿠키 값은 이미 순수 JWT 토큰 상태
                    break;
                }
            }
        }

        log.debug("token validate Test: {}", token);
        */

        // 3. 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new ServiceErrorException(ErrorEnum.ERR_NOT_LOGIN_ACCESS);
        }

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), true));
    }
}
