package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminResponse;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signup(@Valid SignUpRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        String encodePassword = passwordEncoder.encode(request.getPassword());
        Admin admin = Admin.regist(request.getEmail(), encodePassword, request.getAdminName(), request.getPhone(), request.getRole(), request.getRequestMessage());
        Admin saveAdmin = adminRepository.save(admin);
        return SignUpResponse.regist(
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
    public SessionAdmin login(LoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalAccessError("이메일이 틀렸습니다")  //TODO: 추후 전역 예외처리 필요
        );

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                admin.getPassword()
        );

        if (!matches){
            throw new IllegalAccessError("비밀번호가 틀렸습니다");
        }

        // 관리자 상태별 로그인 제한
        switch (admin.getStatus()) {
            case WAIT:
                throw new IllegalAccessError("해당 계정은 승인 대기 중입니다");
            case DENY:
                throw new IllegalAccessError("해당 계정은 관리자 신청이 거부되었습니다");
            case SUSPEND:
                throw new IllegalAccessError("해당 계정은 정지된 상태입니다");
            case IN_ACT:
                throw new IllegalAccessError("해당 계정은 비활성화된 상태입니다");
            case ACT:
                // 정상 로그인 → 통과
                break;
            default:
                throw new IllegalAccessError("알 수 없는 계정 상태입니다");
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
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 승인/거부할 수 있습니다");
        }

        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다"));

        admin.accept();

        return AcceptAdminResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getRequestMessage(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }

    @Transactional
    public DeniedAdminResponse denyAdmin(
            Long targetAdminId,
            SessionAdmin loginAdmin,
            @Valid DeniedAdminRequest request
    ) {

        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 승인/거부할 수 있습니다");
        }

        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다"));

        admin.deny(request.getDeniedReason());

        return DeniedAdminResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getRequestMessage(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }

    //관리자 리스트 조회
    @Transactional(readOnly = true)
    public Page<SearchAdminListResponse> getAdminList(String keyword, int page, int size, String sortBy, String direction, AdminRole role, AdminStatus status, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 관리자 리스트 조회가 가능합니다");
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Admin> admins = adminRepository.findAdmins(
                (keyword == null || keyword.isBlank()) ? null : keyword,
                role,
                status,
                pageable
        );

        return admins.map(a -> SearchAdminListResponse.regist(
                a.getAdminId(),
                a.getAdminName(),
                a.getEmail(),
                a.getPhone(),
                a.getRole(),
                a.getStatus(),
                a.getCreatedAt(),
                a.getAcceptedAt()
        ));

    }

    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public SearchAdminDetailResponse getAdminDetail(Long adminId, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 접근할 수 있습니다");
        }

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다")
        );

        if (admin.getDeleted()) {
            throw new IllegalStateException("삭제된 관리자입니다");
        }
        return SearchAdminDetailResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt(),
                admin.getRequestMessage(),
                admin.getDeniedReason()
        );
    }

    // 관리자 정보 수정
    @Transactional
    public UpdateAdminResponse updateAdmin(Long adminId, @Valid UpdateAdminRequest request, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 접근할 수 있습니다");
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다")
        );

        // 이메일 중복 체크 (본인 제외)
        if (!admin.getEmail().equals(request.getEmail())) {
            if (adminRepository.existsByEmailAndAdminIdNot(request.getEmail(), adminId)){
                throw new IllegalStateException("이미 사용 중인 이메일입니다");
            }
        }

        admin.update(request.getAdminName(), request.getEmail(), request.getPhone());

        return UpdateAdminResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getUpdatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }

    // 관리자 삭제
    @Transactional
    public void deleteAdmin(Long adminId, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 접근할 수 있습니다");
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다")
        );

        if (admin.getAdminId().equals(loginAdmin.getAdminId())) {
            throw new IllegalStateException("본인 계정은 삭제할 수 없습니다");
        }

        admin.delete();
    }

    // 내 프로필 조회 (로그인 사용자)
    @Transactional(readOnly = true)
    public GetMyProfileResponse getMyProfile(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다")
        );
        if (admin.getDeleted().equals(true)) {
            throw new IllegalStateException("삭제된 관리자입니다");
        }
        return GetMyProfileResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }

    // 내 프로필 수정 (로그인 사용자)
    @Transactional
    public UpdateMyProfileResponse updateMyProfile(UpdateMyProfileRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다")
        );

        if (admin.getDeleted().equals(true)) {
            throw new IllegalStateException("삭제된 관리자입니다");
        }

        if (adminRepository.existsByEmailAndAdminIdNot(request.getEmail(), adminId)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다");
        }

        admin.update(
                request.getAdminName(),
                request.getEmail(),
                request.getPhone()
        );

        return UpdateMyProfileResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }

    // 내 비밀번호 변경 (로그인 사용자)
    @Transactional
    public void changeMyPassword(ChangeMyPasswordRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다")
        );

        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        }

        if (passwordEncoder.matches(request.getNewPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 기존 비밀번호와 같습니다");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        admin.changePassword(encodedPassword);
    }

    // 관리자 역할 변경
    @Transactional
    public ChangeAdminRoleResponse changeAdminRole(ChangeAdminRoleRequest request, Long adminId, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 접근할 수 있습니다");
        }

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다")
        );

        admin.changeAdminRole(request.getRole());

        return ChangeAdminRoleResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }

    //관리자 상태 변경
    @Transactional
    public ChangeAdminStatusResponse changeAdminStatus(ChangeAdminStatusRequest request, Long adminId, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new IllegalAccessError("슈퍼 관리자만 접근할 수 있습니다");
        }

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 관리자입니다")
        );

        admin.changeAdminStatus(request.getStatus());

        return ChangeAdminStatusResponse.regist(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt()
        );
    }
}
