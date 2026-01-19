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
    "createdAt",
    "status",
    "requestMessage"
})
public class SignUpResponse {

    private final Long adminId;
    private final String adminName;
    private final String email;
    private final String phone;
    private final String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    private final String status;
    private final String requestMessage;

    private SignUpResponse(
            Long adminId,
            String adminName,
            String email,
            String phone,
            String role,
            LocalDateTime createdAt,
            String status,
            String requestMessage
    ) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.status = status;
        this.requestMessage = requestMessage;
    }

    public static SignUpResponse register(
            Long adminId,
            String adminName,
            String email,
            String phone,
            AdminRole role,
            LocalDateTime createdAt,
            AdminStatus status,
            String requestMessage
    ) {
        return new SignUpResponse(
                adminId,
                adminName,
                email,
                phone,
                role.getRoleCode(),
                createdAt,
                status.getStatusCode(),
                requestMessage
        );
    }
}

