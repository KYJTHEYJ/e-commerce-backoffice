package e3i2.ecommerce_backoffice.domain.product.dto;

import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import lombok.Getter;

@Getter
public class UpdateStatusProductRequest {
    private Long id;
    private ProductStatus status;
}
