package e3i2.ecommerce_backoffice.domain.product.dto;

import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static e3i2.ecommerce_backoffice.common.util.Constants.*;

@Getter
public class CreateProductRequest {
    @NotBlank(message = MSG_PRODUCT_NAME_BLANK_ERR)
    private String productName;
    @NotNull(message = MSG_PRODUCT_CATEGORY_NULL_ERR)
    private ProductCategory category;
    @NotNull(message = MSG_PRODUCT_PRICE_NULL_ERR)
    @Min(value = 0, message = MSG_PRODUCT_PRICE_MINUS_ERR)
    @Max(value = Long.MAX_VALUE, message = MSG_PRODUCT_PRICE_MAX_ERR)
    private Long price;
    @NotNull(message = MSG_PRODUCT_STATUS_NULL_ERR)
    private ProductStatus status;
    @NotNull(message = MSG_PRODUCT_QUANTITY_NULL_ERR)
    @Max(value = Long.MAX_VALUE, message = MSG_PRODUCT_QUANTITY_MAX_ERR)
    private Long quantity;
}
