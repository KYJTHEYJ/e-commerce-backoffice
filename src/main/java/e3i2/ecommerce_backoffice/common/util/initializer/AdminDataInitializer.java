package e3i2.ecommerce_backoffice.common.util.initializer;

import e3i2.ecommerce_backoffice.common.config.PasswordEncoder;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("adminDataInit")
@Profile("local")
@RequiredArgsConstructor
@Order(1)
public class AdminDataInitializer implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (adminRepository.count() > 0) {
            return;
        }

        Admin admin = Admin.register(
            "admin@test.com"
            , passwordEncoder.encode("test1234")
            ,"TEST_SUPER_ADMIN"
            , "010-0000-0000"
            , AdminRole.SUPER_ADMIN
            , AdminStatus.ACT
            ,"테스트용 슈퍼 관리자"
        );

        adminRepository.save(admin);
    }
}
