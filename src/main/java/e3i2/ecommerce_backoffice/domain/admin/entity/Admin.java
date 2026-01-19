package e3i2.ecommerce_backoffice.domain.admin.entity;

import e3i2.ecommerce_backoffice.common.entity.Base;
import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
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

    private String deniedReason;
    private LocalDateTime deniedAt;

    @Column(length = 500)
    private String requestMessage;

    public static Admin register(String email, String password, String adminName, String phone, AdminRole role, AdminStatus status, String requestMessage) {
        Admin admin = new Admin();
        admin.email = email;
        admin.password = password;
        admin.adminName = adminName;
        admin.phone = phone;
        admin.role = role;
        admin.status = status;
        admin.deleted = false;
        admin.requestMessage = requestMessage;

        return admin;
    }

    public void update(String adminName, String email, String phone) {
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeAdminRole(AdminRole role) {
        this.role = role;
    }

    public void changeAdminStatus(AdminStatus status) {
        this.status = status;
    }

    public void accept() {
        if (this.status != AdminStatus.WAIT) {
            throw new ServiceErrorException(ErrorEnum.ERR_NOT_ADMIN_STATUS_WAIT);
        }

        this.status = AdminStatus.ACT;
        this.acceptedAt = LocalDateTime.now();
    }

    public void deny(String reason) {
        if (this.status != AdminStatus.WAIT) {
            throw new IllegalStateException("해당 계정은 승인 대기 상태가 아닙니다");
        }

        this.status = AdminStatus.DENY;
        this.deniedReason = reason;
        this.deniedAt = LocalDateTime.now();
    }

    public void delete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }
}


