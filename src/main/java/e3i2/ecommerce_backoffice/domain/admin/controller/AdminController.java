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
import org.springframework.web.context.request.RequestAttributes;

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
    public ResponseEntity<AdminApiResponse<ApproveAdminResponse>> approveAdmin(
            @PathVariable Long adminId,
            @SessionAttribute("loginAdmin") SessionAdmin loginAdmin
    ) {
        ApproveAdminResponse response = adminService.approveAdmin(adminId, loginAdmin);

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
            @SessionAttribute("loginAdmin") SessionAdmin loginAdmin
    ) {

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
    public ResponseEntity<GetMyProfileResponse> getMyProfile(HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getMyProfile(adminId));
    }

    // 내 프로필 수정
    @PutMapping("/me/profile")
    public ResponseEntity<UpdateMyProfileResponse> updateMyProfile(@Valid @RequestBody UpdateMyProfileRequest request, HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateMyProfile(request, adminId));
    }

    // 내 비밀번호 변경
    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(@Valid @RequestBody ChangeMyPasswordRequest request, HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.ChangeMyPassword(request, adminId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 관리자 역할 변경
    @PutMapping("/{adminId}/role")
    public void changeAdminRole(@PathVariable Long adminId, @Valid @RequestBody ChangeAdminRoleRequest request, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.ChangeAdminRole(request, adminId);
    }

    // 관리자 상태 변경
    @PutMapping("/{adminId}/status")
    public void changeAdminStatus(@PathVariable Long adminId, @Valid @RequestBody ChangeAdminStatusRequest request, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.ChangeAdminStatus(request, adminId);
    }
}
