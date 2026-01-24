package e3i2.ecommerce_backoffice.common.auth.service;

import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String adminEmail) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(adminEmail).orElseThrow(() -> new UsernameNotFoundException("관리자 계정을 찾을 수 없습니다"));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole().getRoleCode()) // 역할 설정 (자동으로 GrantedAuthority 객체 생성), [GrantedAuthority(authority="ROLE_SUPER_ADMIN")] 같은
                .build();
    }
}
