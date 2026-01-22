package e3i2.ecommerce_backoffice.common.auth.controller;

import e3i2.ecommerce_backoffice.common.auth.dto.LoginRequest_NEW;
import e3i2.ecommerce_backoffice.common.auth.dto.LoginResponse_NEW;
import e3i2.ecommerce_backoffice.common.auth.service.AuthService;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_NOT_LOGIN_ACCESS;

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
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(),  authService.login_new(request)));
    }

    //TODO JWT 테스트
    @GetMapping("/validate_NEW")
    public ResponseEntity<DataResponse<Boolean>> validate_new(
            HttpServletRequest request
    ) {
        // Bearer 인증 값을 제외한 나머지 값인 토큰
        String token = request.getHeader("Authorization").substring("Bearer ".length());

        if(!jwtUtil.validateToken(token)) {
            throw new ServiceErrorException(ErrorEnum.ERR_NOT_LOGIN_ACCESS);
        }

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), true));
    }
}
