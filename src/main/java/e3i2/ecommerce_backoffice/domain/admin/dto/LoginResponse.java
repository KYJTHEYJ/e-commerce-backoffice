package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

// 생성자 정적 팩토리 메서드로 권장
@Getter
public class LoginResponse {
    private final Long adminId;
    private final String adminName;
    private final String email;
    private final String phone;
    private final AdminRole role;
    private final AdminStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime acceptedAt;

    public LoginResponse(SessionAdmin sessionAdmin) {
        this.adminId = sessionAdmin.getAdminId();
        this.adminName = sessionAdmin.getAdminName();
        this.email = sessionAdmin.getEmail();
        this.phone = sessionAdmin.getPhone();
        this.role = sessionAdmin.getRole();
        this.status = sessionAdmin.getStatus();
        this.createdAt = sessionAdmin.getCreatedAd();
        this.acceptedAt = sessionAdmin.getAcceptedAt();
    }

    //거부 시간은 따로 정의하지 않았음
}