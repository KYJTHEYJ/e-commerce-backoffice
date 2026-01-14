package e3i2.ecommerce_backoffice.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeniedAdminRequest {
    @NotBlank(message = "거부 사유를 입력해주세요")
    private String deniedReason;
}
