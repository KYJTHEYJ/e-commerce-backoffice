package e3i2.ecommerce_backoffice.domain.admin.repository;

import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(@Email(message = "이메일 형식으로 작성해야 합니다.") String email);

    boolean existsByEmailAndAdminIdNot(String email, Long adminId);

    Optional<Admin> findByAdminIdAndDeletedFalse(Long adminId);

    @Query("""
            SELECT a
            FROM Admin a
            WHERE (
                :keyword IS NULL
                OR LOWER(a.adminName) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR LOWER(a.email) LIKE CONCAT('%', LOWER(:keyword), '%')
            )
            AND (a.role = :role OR :role IS NULL)
            AND (a.status = :status OR :status IS NULL)
            AND a.deleted = false
           """)
    Page<Admin> findAdmins(
            @Param("keyword") String keyword,
            @Param("role") AdminRole role,
            @Param("status") AdminStatus status,
            Pageable pageable
    );

    boolean existsByEmail(@Email(message = "이메일 형식으로 작성해야 합니다") String email);
}
