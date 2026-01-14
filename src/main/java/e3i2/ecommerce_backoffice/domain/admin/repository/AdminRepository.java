package e3i2.ecommerce_backoffice.domain.admin.repository;

import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(@Email(message = "이메일 형식으로 작성해야 합니다.") String email);
}
