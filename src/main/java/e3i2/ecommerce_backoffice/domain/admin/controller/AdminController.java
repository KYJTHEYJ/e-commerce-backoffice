package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;
    private final RequestAttributes requestAttributes;

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

    // 관리자 승인
    @PutMapping("{adminId}/accept")
    public void acceptAdmin(@PathVariable Long adminId, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.acceptAdmin(adminId);
    }

    // 관리자 거부
    @PutMapping("{adminId}/deny")
    public void denyAdmin(@PathVariable Long adminId, @Valid @RequestBody AdminDenyRequest request, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        adminService.denyAdmin(request, adminId);
    }
}
