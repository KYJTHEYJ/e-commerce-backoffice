package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponse {
    private final Long adminId;
    private final String adminName;
    private final String email;
    private final String phone;
    private final AdminRole role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;
    private final AdminStatus status;
    private final String requestMessage;

    public SignupResponse(Long adminId, String adminName, String email, String phone, AdminRole role, LocalDateTime createdAt, AdminStatus status, String requestMessage) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.status = status;
        this.requestMessage = requestMessage;
    }
}
