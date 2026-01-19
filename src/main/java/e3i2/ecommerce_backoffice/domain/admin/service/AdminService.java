package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminResponse;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUp(@Valid SignUpRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new ServiceErrorException(ERR_DUPLICATED_EMAIL);
        }

        if(request.getRole() == AdminRole.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_ADMIN_ROLE);
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        Admin admin = Admin.register(request.getEmail(), encodePassword, request.getAdminName(), request.getPhone(), request.getRole(), AdminStatus.WAIT, request.getRequestMessage());
        Admin saveAdmin = adminRepository.save(admin);

        return SignUpResponse.register(
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
                () -> new ServiceErrorException(ERR_WRONG_EMAIL_PASSWORD)
        );

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                admin.getPassword()
        );

        if (!matches){
            throw new ServiceErrorException(ERR_WRONG_EMAIL_PASSWORD);
        }

        // 관리자 상태별 로그인 제한
        switch (admin.getStatus()) {
            case WAIT:
                throw new ServiceErrorException(ERR_WAIT_ADMIN_ACCOUNT_LOGIN);
            case DENY:
                throw new ServiceErrorException(ERR_DENY_ADMIN_ACCOUNT_LOGIN);
            case SUSPEND:
                throw new ServiceErrorException(ERR_SUSPEND_ADMIN_ACCOUNT_LOGIN);
            case IN_ACT:
                throw new ServiceErrorException(ERR_IN_ACT_ADMIN_ACCOUNT_LOGIN);
            case ACT:
                // 정상 로그인 → 통과
                break;
            default:
                throw new ServiceErrorException(ERR_UNAUTHORIZED_ACCOUNT_LOGIN);
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
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));

        admin.accept();

        return AcceptAdminResponse.register(
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
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findById(targetAdminId)
                .orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));

        admin.deny(request.getDeniedReason());

        return DeniedAdminResponse.register(
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
    public ItemsWithPagination<List<SearchAdminDetailResponse>> getAdminList (
            String keyword, int page, int limit, String sortBy, String sortOrder, AdminRole role, AdminStatus status, SessionAdmin loginAdmin
    ) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<Admin> admins = adminRepository.findAdmins(
                (keyword == null || keyword.isBlank()) ? null : keyword,
                role,
                status,
                pageable
        );

        List<SearchAdminDetailResponse> items = admins.stream()
                .map(a -> {
                    String requestMessage = null;
                    String deniedReason = null;

                    if (a.getStatus() == AdminStatus.WAIT) {
                        requestMessage = a.getRequestMessage();
                    }
                    else if (a.getStatus() == AdminStatus.DENY) {
                        deniedReason = a.getDeniedReason();
                    }

                    return SearchAdminDetailResponse.register(
                            a.getAdminId(),
                            a.getAdminName(),
                            a.getEmail(),
                            a.getPhone(),
                            a.getRole(),
                            a.getStatus(),
                            a.getCreatedAt(),
                            a.getAcceptedAt(),
                            a.getDeniedAt(),
                            requestMessage,
                            deniedReason
                    );
                })
                .toList();

        return ItemsWithPagination.register(items, page, limit, admins.getTotalElements());
    }

    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public SearchAdminDetailResponse getAdminDetail(Long adminId, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );


        String requestMessage = null;
        String deniedReason = null;

        if (admin.getStatus() == AdminStatus.WAIT) {
            requestMessage = admin.getRequestMessage();
        } else if (admin.getStatus() == AdminStatus.DENY) {
            deniedReason = admin.getDeniedReason();
        }

        return SearchAdminDetailResponse.register(
                admin.getAdminId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getAcceptedAt(),
                admin.getDeniedAt(),
                requestMessage,
                deniedReason
        );
    }

    // 관리자 정보 수정
    @Transactional
    public UpdateAdminResponse updateAdmin(Long adminId, @Valid UpdateAdminRequest request, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        if (!admin.getEmail().equals(request.getEmail())) {
            if (adminRepository.existsByEmailAndAdminIdNot(request.getEmail(), adminId)){
                throw new ServiceErrorException(ERR_DUPLICATED_EMAIL);
            }
        }

        admin.update(request.getAdminName(), request.getEmail(), request.getPhone());

        return UpdateAdminResponse.register(
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
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        if (admin.getAdminId().equals(loginAdmin.getAdminId())) {
            throw new ServiceErrorException(ERR_DELETED_ADMIN_SELF);
        }

        admin.delete();
    }

    // 내 프로필 조회 (로그인 사용자)
    @Transactional(readOnly = true)
    public GetMyProfileResponse getMyProfile(Long adminId) {
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        return GetMyProfileResponse.register(
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
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        if (adminRepository.existsByEmailAndAdminIdNot(request.getEmail(), adminId)) {
            throw new ServiceErrorException(ERR_DUPLICATED_EMAIL);
        }

        admin.update(
                request.getAdminName(),
                request.getEmail(),
                request.getPhone()
        );

        return UpdateMyProfileResponse.register(
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
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new ServiceErrorException(ERR_NOT_MATCH_PASSWORD);
        }

        if (passwordEncoder.matches(request.getNewPassword(), admin.getPassword())) {
            throw new ServiceErrorException(ERR_SAME_OLD_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        admin.changePassword(encodedPassword);
    }

    // 관리자 역할 변경
    @Transactional
    public ChangeAdminRoleResponse changeAdminRole(@Valid ChangeAdminRoleRequest request, Long adminId, SessionAdmin loginAdmin) {
        if (loginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        if(request.getRole() == AdminRole.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_ADMIN_ROLE);
        }

        admin.changeAdminRole(request.getRole());

        return ChangeAdminRoleResponse.register(
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
            throw new ServiceErrorException(ERR_ONLY_SUPER_ADMIN_ACCESS);
        }

        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(adminId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN)
        );

        if(request.getStatus() == AdminStatus.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_ADMIN_STATUS);
        }

        admin.changeAdminStatus(request.getStatus());

        return ChangeAdminStatusResponse.register(
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
