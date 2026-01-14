package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionAdmin {
    private final Long adminId;
    private final String email;
    private final String adminName;
    private final String phone;
    private final AdminRole role;
    private final AdminStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime acceptedAt;

    public SessionAdmin(Long adminId, String email, String adminName, String phone, AdminRole role, AdminStatus status, LocalDateTime createdAd, LocalDateTime acceptedAt) {
        this.adminId = adminId;
        this.email = email;
        this.adminName = adminName;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAd = createdAd;
        this.acceptedAt = acceptedAt;
    }
}
