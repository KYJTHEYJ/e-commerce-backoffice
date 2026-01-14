package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.AdminApiResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import static e3i2.ecommerce_backoffice.common.util.Constants.ADMIN_SESSION_NAME;


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

    //관리자 회원 가입 요청 승인
    @PutMapping("/{adminId}/accept")
    public ResponseEntity<AdminApiResponse<AcceptAdminResponse>> acceptAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {


        AcceptAdminResponse response = adminService.acceptAdmin(adminId, loginAdmin);

        return ResponseEntity.ok(
                AdminApiResponse.success(
                        "OK",
                        "관리자가 승인되었습니다.",
                        response
                )
        );
    }

    //관리자 회원 가입 요청 거부
    @PutMapping("/{adminId}/deny")
    public ResponseEntity<AdminApiResponse<DeniedAdminResponse>> denyAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody DeniedAdminRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        DeniedAdminResponse response = adminService.denyAdmin(adminId, loginAdmin, request);

        return ResponseEntity.status(HttpStatus.OK).body(
                AdminApiResponse.success(
                        "OK",
                        "관리자 신청이 거부되었습니다.",
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
        session.setAttribute(ADMIN_SESSION_NAME, sessionAdmin);

        LoginResponse response = new LoginResponse(sessionAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(
                AdminApiResponse.success(
                        "OK",
                        "로그인 성공",
                        response
                )
        );
    }

    //관리자 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<AdminApiResponse<Void>> logout(
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false)
            SessionAdmin sessionUser, HttpSession session){
        if (sessionUser == null) {
            return ResponseEntity.badRequest().build();
        }

        session.invalidate(); //세션 무효화 시켜 로그아웃 상태로 만듦
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminApiResponse<SearchAdminDetailResponse>> getAdminDetail(
            @PathVariable Long adminId
    ) {
        SearchAdminDetailResponse response = adminService.getAdminDetail(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse.success(
                "OK",
                "관리자 상세 조회 성공",
                response
        ));
    }

    // 내 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<GetMyProfileResponse> getMyProfile(
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin) {
        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getMyProfile(loginAdmin.getAdminId()));
    }

    // 내 프로필 수정
    @PutMapping("/me/profile")
    public ResponseEntity<UpdateMyProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateMyProfileRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin) {

        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateMyProfile(request, loginAdmin.getAdminId()));
    }

    // 내 비밀번호 변경
    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @Valid @RequestBody ChangeMyPasswordRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin) {
        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.changeMyPassword(request, loginAdmin.getAdminId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 관리자 역할 변경
    @PutMapping("/{adminId}/role")
    public void changeAdminRole(
            @PathVariable Long adminId,
            @RequestBody ChangeAdminRoleRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin) {
        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.changeAdminRole(request, adminId);
    }

    // 관리자 상태 변경
    @PutMapping("/{adminId}/status")
    public void changeAdminStatus(
            @PathVariable Long adminId, @RequestBody ChangeAdminStatusRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin) {
        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.changeAdminStatus(request, adminId);
    }
}
