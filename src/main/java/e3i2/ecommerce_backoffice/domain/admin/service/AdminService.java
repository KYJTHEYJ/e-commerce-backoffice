package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
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

    @Transactional
    public SignupResponse signup(@Valid SignupRequest request) {
        String encodePassword = passwordEncoder.encode(request.getPassword());
        Admin admin = Admin.regist(request.getEmail(), encodePassword, request.getAdminName(), request.getPhone(), request.getRole());
        Admin saveAdmin = adminRepository.save(admin);
        return new SignupResponse(
                saveAdmin.getAdminId(),
                saveAdmin.getAdminName(),
                saveAdmin.getEmail(),
                saveAdmin.getPhone(),
                saveAdmin.getRole(),
                saveAdmin.getCreatedAt(),
                saveAdmin.getStatus(),
                saveAdmin.getRequestMessage()
        );
    }

    @Transactional
    public SessionAdmin login(@Valid LoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalAccessError("이메일이 틀렸습니다.")  //TODO: 추후 전역 예외처리 필요
        );

        //암호화된 기존의 비밀번호와 입력한 비밀번호 비교
        boolean matches = passwordEncoder.matches(
                request.getPassword(), //request로 받은 비밀번호(평문)
                admin.getPassword() //기존에 저장되어 있던 암호화된 비밀번호(암호문)
        );
        if (!matches){
            throw new IllegalAccessError("비밀번호가 틀렸습니다.");
        }

        //활성 상태가 아닐 경우 예외 처리
        if(admin.getStatus() != AdminStatus.ACT){
            throw new IllegalAccessError("해당 계정은 활성 상태가 아닙니다.");
        }

        return new SessionAdmin(
                admin.getAdminId(),
                admin.getEmail(),
                admin.getAdminName(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt()
        );
    }

    @Transactional
    public ApproveAdminResponse approveAdmin(Long targetAdminId, SessionAdmin loginAdmin) {
        //슈퍼 관리자 권한 확인
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 승인/거부할 수 있습니다.");
        }
        //승인 대상 관리자 조회
        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        //상태 변경
        admin.approve();

        return new ApproveAdminResponse(admin);
    }

    @Transactional
    public DeniedAdminResponse denyAdmin(
            Long targetAdminId,
            SessionAdmin loginAdmin,
            @Valid DeniedAdminRequest request
    ) {

        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 승인/거부할 수 있습니다.");
        }

        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        admin.deny(request.getDeniedReason());

        return new DeniedAdminResponse(admin);
    }

}
