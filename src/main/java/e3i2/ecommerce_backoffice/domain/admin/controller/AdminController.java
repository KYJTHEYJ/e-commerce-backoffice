package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.domain.admin.dto.LoginRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.LoginResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.SignupRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.SignupResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.AdminApiResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    //관리자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<AdminApiResponse<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response =  adminService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body( AdminApiResponse.success(
                "CREATED",
                "회원가입 신청이 완료되었습니다. 관리자 승인을 기다려주세요.",
                response
            )
        );
    }

    //관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<AdminApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        SessionAdmin sessionAdmin = adminService.login(request);
        session.setAttribute("loginAdmin", sessionAdmin);

        LoginResponse response = new LoginResponse(sessionAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(
                AdminApiResponse.success(
                        "OK",
                        "로그인 성공",
                        response
                )
        );
    }
}
