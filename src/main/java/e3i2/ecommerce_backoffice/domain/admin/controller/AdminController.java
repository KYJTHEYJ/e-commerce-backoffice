package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.AdminApiResponse;
import e3i2.ecommerce_backoffice.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

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


}
