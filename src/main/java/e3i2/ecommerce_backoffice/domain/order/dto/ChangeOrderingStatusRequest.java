package e3i2.ecommerce_backoffice.domain.order.dto;

import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_ORDER_STATUS_BLANK_ERR;

@Getter
public class ChangeOrderingStatusRequest {
    @NotNull(message = MSG_ORDER_STATUS_BLANK_ERR)
    private OrderingStatus status;
}
