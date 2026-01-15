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
public class UpdateMyProfileResponse {

    private Long adminId;
    private String adminName;
    private String email;
    private String phone;
    private AdminRole role;
    private AdminStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime acceptedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime deniedAt;

    public static UpdateMyProfileResponse regist(
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
        UpdateMyProfileResponse response = new UpdateMyProfileResponse();

        response.adminId = adminId;
        response.adminName = adminName;
        response.email = email;
        response.phone = phone;
        response.role = role;
        response.status = status;
        response.createdAt = createdAt;
        response.acceptedAt = acceptedAt;
        response.deniedAt = deniedAt;

        return response;
    }
}

