package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.dto.SignupRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.SignupResponse;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
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
                saveAdmin.getStatus()
        );
    }
}
