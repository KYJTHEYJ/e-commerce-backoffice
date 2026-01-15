package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.AdminApiResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.AdminApiResponse2;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.AdminApiResponse3;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<AdminApiResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        SignUpResponse response = adminService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(AdminApiResponse.success(
                        "201 CREATED",
                        "회원가입 신청이 완료되었습니다. 관리자 승인을 기다려주세요",
                        response
                )
        );
    }

    //관리자 회원 가입 요청 승인
    @PutMapping("/{adminId}/accept")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<AcceptAdminResponse>> acceptAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        AcceptAdminResponse response = adminService.acceptAdmin(adminId, loginAdmin);

        return ResponseEntity.ok(
                AdminApiResponse.success(
                        "200 OK",
                        "관리자가 승인되었습니다",
                        response
                )
        );
    }

    //관리자 회원 가입 요청 거부
    @PutMapping("/{adminId}/deny")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<DeniedAdminResponse>> denyAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody DeniedAdminRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        DeniedAdminResponse response = adminService.denyAdmin(adminId, loginAdmin, request);

        return ResponseEntity.status(HttpStatus.OK).body(
                AdminApiResponse.success(
                        "200 OK",
                        "관리자 신청이 거부되었습니다",
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
        // 이미 로그인이 되어있는 상태에서 로그인 시 그냥 200 OK 코드 반환
        // 다른 처리 방법이 좋겠다 하시는 담당자 분은 수정 해서 처리하셔도 됩니다
        if (session.getAttribute(ADMIN_SESSION_NAME) != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        SessionAdmin sessionAdmin = adminService.login(request);
        session.setAttribute(ADMIN_SESSION_NAME, sessionAdmin);

        LoginResponse response = LoginResponse.regist(
                sessionAdmin.getAdminId(),
                sessionAdmin.getAdminName(),
                sessionAdmin.getEmail(),
                sessionAdmin.getPhone(),
                sessionAdmin.getRole(),
                sessionAdmin.getStatus(),
                sessionAdmin.getCreatedAd(),
                sessionAdmin.getAcceptedAt()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                AdminApiResponse.success(
                        "200 OK",
                        "로그인 성공",
                        response
                )
        );
    }

    //관리자 로그아웃
    @PostMapping("/logout")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<Void>> logout(
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false)
            SessionAdmin loginAdmin, HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //관리자 리스트 조회
    @GetMapping
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<Page<SearchAdminListResponse>>> getAdminList (
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) AdminRole role,
            @RequestParam(required = false) AdminStatus status,
            @SessionAttribute(ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        Page<SearchAdminListResponse> response = adminService.getAdminList(
                keyword, page, size, sortBy, direction, role, status, loginAdmin
        );

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse.success(
                "OK",
                "관리자 리스트 조회 성공",
                response
        ));
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<SearchAdminDetailResponse>> getAdminDetail(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        SearchAdminDetailResponse response = adminService.getAdminDetail(adminId, loginAdmin);
        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse.success(
                "OK",
                "관리자 상세 조회 성공",
                response
        ));
    }

    // 관리자 정보 수정
    @PutMapping("/{adminId}")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<UpdateAdminResponse>> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        UpdateAdminResponse response = adminService.updateAdmin(adminId, request, loginAdmin);
        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse.success(
                "OK",
                "관리자 정보 수정 성공",
                response
        ));
    }

    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<Void>> deleteAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        adminService.deleteAdmin(adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse.success(
                "OK",
                "사용자가 삭제되었습니다",
                null
        ));
    }

    // 내 프로필 조회
    @GetMapping("/me")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse2<GetMyProfileResponse>> getMyProfile(
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        GetMyProfileResponse response = adminService.getMyProfile(loginAdmin.getAdminId());

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse2.success("OK", response));
    }

    // 내 프로필 수정
    @PutMapping("/me/profile")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse<UpdateMyProfileResponse>> updateMyProfile(
            @Valid @RequestBody UpdateMyProfileRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        UpdateMyProfileResponse response = adminService.updateMyProfile(request, loginAdmin.getAdminId());

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse.success("OK","프로필이 성공적으로 업데이트되었습니다.", response));
    }

    // 내 비밀번호 변경
    @PutMapping("/me/password")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse3> changeMyPassword(
            @Valid @RequestBody ChangeMyPasswordRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        adminService.changeMyPassword(request, loginAdmin.getAdminId());

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse3.success("OK", "비밀번호가 성공적으로 변경되었습니다."));
    }

    // 관리자 역할 변경
    @PutMapping("/{adminId}/role")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse2<ChangeAdminRoleResponse>> changeAdminRole(
            @PathVariable Long adminId,
            @RequestBody ChangeAdminRoleRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        ChangeAdminRoleResponse response = adminService.changeAdminRole(request, adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse2.success("OK", response));
    }

    // 관리자 상태 변경
    @PutMapping("/{adminId}/status")
    @LoginSessionCheck
    public ResponseEntity<AdminApiResponse2<ChangeAdminStatusResponse>> changeAdminStatus(
            @PathVariable Long adminId,
            @RequestBody ChangeAdminStatusRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        ChangeAdminStatusResponse response = adminService.changeAdminStatus(request, adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(AdminApiResponse2.success("OK", response));
    }
}
