package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AcceptAdminResponse {
    private final Long adminId;
    private final String adminName;
    private final String email;
    private final String phone;
    private final AdminRole role;
    private final AdminStatus status;
    private final String requestMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime acceptedAt;

    private final LocalDateTime deniedAt;

    public AcceptAdminResponse(Admin admin) {
        this.adminId = admin.getAdminId();
        this.adminName = admin.getAdminName();
        this.email = admin.getEmail();
        this.phone = admin.getPhone();
        this.role = admin.getRole();
        this.status = admin.getStatus();
        this.requestMessage = admin.getRequestMessage();
        this.createdAt = admin.getCreatedAt();
        this.acceptedAt = admin.getAcceptedAt();
        this.deniedAt = admin.getDeniedAt();
    }
}
