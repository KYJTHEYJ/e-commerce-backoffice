package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 관리자 상세 조회
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchAdminDetailResponse {
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

    // 관리자 상태별 조건부 필드
    private String requestMessage;  // WAIT 상태일 때만
    private String deniedReason; // DENY 상태일 때만

    public static SearchAdminDetailResponse regist(
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
        SearchAdminDetailResponse response = new SearchAdminDetailResponse();

        response.adminId = adminId;
        response.adminName = adminName;
        response.email = email;
        response.phone = phone;
        response.role = role;
        response.status = status;
        response.createdAt = createdAt;
        response.acceptedAt = acceptedAt;
        response.deniedAt = deniedAt;

        // 상태에 따른 조건부 필드 설정
        if (status == AdminStatus.WAIT){
            response.requestMessage = requestMessage;
            response.deniedReason = null;
        }
        else if(status == AdminStatus.DENY){
            response.requestMessage = null;
            response.deniedReason = deniedReason;
        } else {
            response.requestMessage = null;
            response.deniedReason = null;
        }

        return response;
    }
}
