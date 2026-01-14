package e3i2.ecommerce_backoffice.domain.admin.service;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.dto.LoginRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.SignupRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.SignupResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
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
                saveAdmin.getStatus()
        );
    }

    @Transactional
    public SessionAdmin login(@Valid LoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalAccessError("이메일 또는 비밀번호가 틀렸습니다.")
        );

        //암호화된 기존의 비밀번호와 입력한 비밀번호 비교
        boolean matches = passwordEncoder.matches(
                request.getPassword(), //request로 받은 비밀번호(평문)
                admin.getPassword() //기존에 저장되어 있던 암호화된 비밀번호(암호문)
        );
        if (!matches){
            throw new IllegalAccessError("이메일 또는 비밀번호가 틀렸습니다.");
        }

        //활성 상태가 아닐 경우 예외 처리
        if(admin.getStatus() != AdminStatus.ACT){
            throw new IllegalAccessError("해당 계정은 활성 상태가 아닙니다.");
        }

        return new SessionAdmin(
                admin.getAdminId(),
                admin.getEmail(),
                admin.getAdminName()
        );
    }
}
