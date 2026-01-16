package e3i2.ecommerce_backoffice.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CancelOrderingRequest {
    @NotBlank(message = "취소 이유는 필수입니다")
    private String cancelReason;
}
