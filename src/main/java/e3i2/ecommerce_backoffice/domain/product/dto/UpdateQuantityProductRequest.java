package e3i2.ecommerce_backoffice.domain.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_PRODUCT_QUANTITY_NULL_ERR;
import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_PRODUCT_QUANTITY_MAX_ERR;

@Getter
public class UpdateQuantityProductRequest {
    @NotNull(message = MSG_PRODUCT_QUANTITY_NULL_ERR)
    @Max(value = Long.MAX_VALUE, message = MSG_PRODUCT_QUANTITY_MAX_ERR)
    private Long quantity;
}
