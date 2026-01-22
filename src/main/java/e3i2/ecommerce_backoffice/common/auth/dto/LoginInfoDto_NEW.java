package e3i2.ecommerce_backoffice.common.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "adminId",
        "adminName",
        "email",
        "phone",
        "role",
        "status",
        "createdAt",
        "acceptedAt"
})
public record LoginInfoDto_NEW(
        Long adminId
        , String adminName
        , String email
        , String phone
        , AdminRole role
        , AdminStatus status
        , @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDateTime createdAt
        , @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDateTime acceptedAt
) {
}
