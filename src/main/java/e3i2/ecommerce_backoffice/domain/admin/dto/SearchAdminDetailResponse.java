package e3i2.ecommerce_backoffice.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

// 관리자 상세 조회
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchAdminDetailResponse {
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime deniedAt;

    // 관리자 상태별 조건부 필드
    private final String requestMessage;  // WAIT 상태일 때만
    private final String deniedReason; // DENY 상태일 때만

    public SearchAdminDetailResponse(Admin admin) {
        this.adminId = admin.getAdminId();
        this.adminName = admin.getAdminName();
        this.email = admin.getEmail();
        this.phone = admin.getPhone();
        this.role = admin.getRole();
        this.status = admin.getStatus();
        this.createdAt = admin.getCreatedAt();
        this.acceptedAt = admin.getAcceptedAt();
        this.deniedAt = admin.getDeniedAt();

        // 상태에 따른 조건부 필드 설정
        if (admin.getStatus() == AdminStatus.WAIT){
            this.requestMessage = admin.getRequestMessage();
            this.deniedReason = null;
        }
        else if(admin.getStatus() == AdminStatus.DENY){
            this.requestMessage = null;
            this.deniedReason = admin.getDeniedReason();
        } else {
            this.requestMessage = null;
            this.deniedReason = null;
        }
    }


}
