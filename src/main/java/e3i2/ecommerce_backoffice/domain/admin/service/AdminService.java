package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public GetMyProfileResponse getMyProfile(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다.")
        );
        if (admin.getDeleted().equals(true)) {
            throw new IllegalStateException("삭제된 관리자입니다.");
        }
        return new GetMyProfileResponse(
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone()
        );
    }

    @Transactional
    public UpdateMyProfileResponse updateMyProfile(UpdateMyProfileRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다.")
        );
        if (admin.getDeleted().equals(true)) {
            throw new IllegalStateException("삭제된 관리자입니다.");
        }
        if (adminRepository.existsByEmailAndAdminIdNot(request.getEmail(), adminId)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        admin.update(
                request.getAdminName(),
                request.getEmail(),
                request.getPhone()
        );
        return new UpdateMyProfileResponse(
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone()
        );
    }

    @Transactional
    public Void ChangeMyPassword(ChangeMyPasswordRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        if (passwordEncoder.matches(request.getNewPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 기존 비밀번호와 같습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        admin.changePassword(encodedPassword);
        return null;
    }

    @Transactional
    public void ChangeAdminRole(ChangeAdminRoleRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        admin.changeAdminRole(request.getRole());
    }

    @Transactional
    public void ChangeAdminStatus(ChangeAdminStatusRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        admin.changeAdminStatus(request.getStatus());
    }

    public void acceptAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        if(!admin.getStatus().equals(AdminStatus.WAIT)) {
            throw new IllegalArgumentException("승인 대기 상태가 아닙니다.");
        }
        admin.accept();
    }

    public void denyAdmin(AdminDenyRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        if(!admin.getStatus().equals(AdminStatus.WAIT)) {
            throw new IllegalArgumentException("승인 대기 상태가 아닙니다.");
        }
        admin.deny(request.getReason());
    }
}
