package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
    "adminId",
    "adminName",
    "email",
    "phone",
    "role",
    "status",
    "createdAt",
    "acceptedAt",
    "deniedAt"
})
public class ChangeAdminStatusResponse {

    private final Long adminId;
    private final String adminName;
    private final String email;
    private final String phone;
    private final String role;
    private final String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime acceptedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime deniedAt;

    private ChangeAdminStatusResponse(
            Long adminId,
            String adminName,
            String email,
            String phone,
            String role,
            String status,
            LocalDateTime createdAt,
            LocalDateTime acceptedAt,
            LocalDateTime deniedAt
    ) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.acceptedAt = acceptedAt;
        this.deniedAt = deniedAt;
    }

    public static ChangeAdminStatusResponse register(
            Long adminId,
            String adminName,
            String email,
            String phone,
            AdminRole role,
            AdminStatus status,
            LocalDateTime createdAt,
            LocalDateTime acceptedAt,
            LocalDateTime deniedAt
    ) {
        return new ChangeAdminStatusResponse(
                adminId,
                adminName,
                email,
                phone,
                role.getRoleCode(),
                status.getStatusCode(),
                createdAt,
                acceptedAt,
                deniedAt
        );
    }
}

