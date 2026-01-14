package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    // 관리자 상세 조회
    public SearchAdminDetailResponse getAdminDetail(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다.")
        );

        if (admin.getDeleted()) {
            throw new IllegalArgumentException("삭제된 관리자입니다.");
        }
        return new SearchAdminDetailResponse(admin);
    }
}
