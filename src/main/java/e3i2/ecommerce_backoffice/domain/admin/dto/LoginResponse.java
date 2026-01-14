package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

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

    public LoginResponse(Long adminId, String adminName, String email, String phone, AdminRole role, AdminStatus status, LocalDateTime createdAt, LocalDateTime acceptedAt) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.acceptedAt = acceptedAt;
    }

    //거부 시간은 따로 정의하지 않았음
}