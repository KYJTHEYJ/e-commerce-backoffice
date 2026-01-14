package e3i2.ecommerce_backoffice.domain.admin.entity;

import e3i2.ecommerce_backoffice.common.entity.Base;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "admin_unique_email"
                , columnNames = {"email"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String adminName;
    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdminStatus status;

    private LocalDateTime acceptedAt;

    @Column(nullable = false)
    private Boolean deleted;
    private LocalDateTime deletedAt;

    public static Admin regist(String email, String password, String adminName, String phone, AdminRole role) {
        Admin admin = new Admin();
        admin.email = email;
        admin.password = password;
        admin.adminName = adminName;
        admin.phone = phone;
        admin.role = role;
        admin.status = AdminStatus.WAIT;
        admin.deleted = false;

        return admin;
    }

    public void delete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }

    public void restore() {
        deleted = false;
        deletedAt = null;
    }
}


