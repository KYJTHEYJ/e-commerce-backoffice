package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminResponse;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public SearchAdminDetailResponse getAdminDetail(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다.")
        );

        if (admin.getDeleted()) {
            throw new IllegalStateException("삭제된 관리자입니다.");
        }
        return new SearchAdminDetailResponse(admin);
    }

    // 관리자 정보 수정
    @Transactional
    public UpdateAdminResponse updateAdmin(Long adminId, @Valid UpdateAdminRequest request) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다.")
        );

        if (admin.getDeleted()) {
            throw new IllegalStateException("삭제한 관리자는 수정할 수 없습니다.");
        }

        // 이메일 중복 체크(본인 제외)
        if (!admin.getEmail().equals(request.getEmail())) {
            if (adminRepository.existsByEmailAndAdminIdNot(request.getEmail(), adminId)){
                throw new IllegalStateException("이미 사용 중인 이메일입니다.");
            }
        }

        admin.updateInfo(request.getAdminName(), request.getEmail(), request.getPhone());
        //flush
        adminRepository.flush();

        return new UpdateAdminResponse(admin);
    }
}
