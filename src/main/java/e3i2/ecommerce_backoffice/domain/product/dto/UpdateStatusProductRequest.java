package e3i2.ecommerce_backoffice.domain.product.dto;

import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_PRODUCT_STATUS_NULL_ERR;

@Getter
public class UpdateStatusProductRequest {
    @NotNull(message = MSG_PRODUCT_STATUS_NULL_ERR)
    private ProductStatus status;
}
