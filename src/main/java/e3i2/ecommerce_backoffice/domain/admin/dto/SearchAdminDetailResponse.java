package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

// 관리자 상세 조회
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "phone",
        "role",
        "status",
        "createdAt",
        "acceptedAt",
        "deniedAt",
        "requestMessage",
        "deniedReason"
})
public class SearchAdminDetailResponse {

    @JsonProperty("id")
    private final Long adminId;

    @JsonProperty("name")
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

    // 관리자 상태별 조건부 필드
    private final String requestMessage;  // WAIT 상태일 때만
    private final String deniedReason; // DENY 상태일 때만

    private SearchAdminDetailResponse(
            Long adminId,
            String adminName,
            String email,
            String phone,
            AdminRole role,
            AdminStatus status,
            LocalDateTime createdAt,
            LocalDateTime acceptedAt,
            LocalDateTime deniedAt,
            String requestMessage,
            String deniedReason
    ) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
        this.role = role.getRoleCode();
        this.status = status.getStatusCode();
        this.createdAt = createdAt;
        this.acceptedAt = acceptedAt;
        this.deniedAt = deniedAt;
        this.requestMessage = requestMessage;
        this.deniedReason = deniedReason;
    }

    public static SearchAdminDetailResponse register(
            Long adminId,
            String adminName,
            String email,
            String phone,
            AdminRole role,
            AdminStatus status,
            LocalDateTime createdAt,
            LocalDateTime acceptedAt,
            LocalDateTime deniedAt,
            String requestMessage,
            String deniedReason
    ) {
        // 상태별 조건부 필드
        String finalRequestMessage = null;
        String finalDeniedReason = null;

        if (status.getStatusCode().equals(AdminStatus.WAIT.getStatusCode())) {
            finalRequestMessage = requestMessage;
        } else if (status.getStatusCode().equals(AdminStatus.DENY.getStatusCode())) {
            finalDeniedReason = deniedReason;
        }

        return new SearchAdminDetailResponse(
                adminId,
                adminName,
                email,
                phone,
                role,
                status,
                createdAt,
                acceptedAt,
                deniedAt,
                finalRequestMessage,
                finalDeniedReason
        );
    }
}
