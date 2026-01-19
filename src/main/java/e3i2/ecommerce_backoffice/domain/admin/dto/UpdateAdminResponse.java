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
    "updatedAt",
    "acceptedAt",
    "deniedAt"
})
public class UpdateAdminResponse {
    private final Long adminId;
    private final String adminName;
    private final String email;
    private final String phone;
    private final String role;
    private final String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime acceptedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime deniedAt;

    private UpdateAdminResponse(
            Long adminId,
            String adminName,
            String email,
            String phone,
            String role,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
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
        this.updatedAt = updatedAt;
        this.acceptedAt = acceptedAt;
        this.deniedAt = deniedAt;
    }

    public static UpdateAdminResponse register(
            Long adminId,
            String adminName,
            String email,
            String phone,
            AdminRole role,
            AdminStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime acceptedAt,
            LocalDateTime deniedAt
    ) {
        return new UpdateAdminResponse(
                adminId,
                adminName,
                email,
                phone,
                role.getRoleCode(),
                status.getStatusCode(),
                createdAt,
                updatedAt,
                acceptedAt,
                deniedAt
        );
    }

}
