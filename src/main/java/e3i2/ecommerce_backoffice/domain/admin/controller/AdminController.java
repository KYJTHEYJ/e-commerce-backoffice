package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.domain.admin.dto.*;
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
}
