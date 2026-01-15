package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpResponse {

    private Long adminId;
    private String adminName;
    private String email;
    private String phone;
    private AdminRole role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    private AdminStatus status;
    private String requestMessage;

    public static SignUpResponse regist(
            Long adminId,
            String adminName,
            String email,
            String phone,
            AdminRole role,
            LocalDateTime createdAt,
            AdminStatus status,
            String requestMessage
    ) {
        SignUpResponse response = new SignUpResponse();

        response.adminId = adminId;
        response.adminName = adminName;
        response.email = email;
        response.phone = phone;
        response.role = role;
        response.createdAt = createdAt;
        response.status = status;
        response.requestMessage = requestMessage;
        return response;
    }
}

