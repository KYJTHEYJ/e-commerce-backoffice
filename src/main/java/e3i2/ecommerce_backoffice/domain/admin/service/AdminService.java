package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
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
        Admin admin = Admin.regist(request.getEmail(), encodePassword, request.getAdminName(), request.getPhone(), request.getRole(), request.getRequestMessage());
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
    public AcceptAdminResponse acceptAdmin(Long targetAdminId, SessionAdmin loginAdmin) {
        //슈퍼 관리자 권한 확인
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 승인/거부할 수 있습니다.");
        }
        //승인 대상 관리자 조회
        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        //상태 변경
        admin.accept();

        return new AcceptAdminResponse(admin);
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

    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public SearchAdminDetailResponse getAdminDetail(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다.")
        );

        if (admin.getDeleted()) {
            throw new IllegalArgumentException("삭제된 관리자입니다.");
        }
        return new SearchAdminDetailResponse(admin);
    }

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
    public Void changeMyPassword(ChangeMyPasswordRequest request, Long adminId) {
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
    public void changeAdminRole(ChangeAdminRoleRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        admin.changeAdminRole(request.getRole());
    }

    @Transactional
    public void changeAdminStatus(ChangeAdminStatusRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("해당 관리자를 찾을 수 없습니다.")
        );
        admin.changeAdminStatus(request.getStatus());
    }
}
