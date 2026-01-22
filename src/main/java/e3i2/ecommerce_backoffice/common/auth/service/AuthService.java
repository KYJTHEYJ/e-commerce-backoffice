package e3i2.ecommerce_backoffice.common.auth.service;

import e3i2.ecommerce_backoffice.common.auth.dto.LoginInfoDto_NEW;
import e3i2.ecommerce_backoffice.common.auth.dto.LoginRequest_NEW;
import e3i2.ecommerce_backoffice.common.auth.dto.LoginResponse_NEW;
import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.jwt.JwtUtil;
import e3i2.ecommerce_backoffice.domain.admin.dto.LoginRequest;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;
import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_IN_ACT_ADMIN_ACCOUNT_LOGIN;
import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_SUSPEND_ADMIN_ACCOUNT_LOGIN;
import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_UNAUTHORIZED_ACCOUNT_LOGIN;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //TODO JWT 테스트
    @Transactional
    public LoginResponse_NEW login_new(LoginRequest_NEW request) {
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

        // JWT 토큰 생성 후 return
        String token = jwtUtil.generateToken(admin.getEmail());

        return new LoginResponse_NEW (
                token
        );
    }
}
