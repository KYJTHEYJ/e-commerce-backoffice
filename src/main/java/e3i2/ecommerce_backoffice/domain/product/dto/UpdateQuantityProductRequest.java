package e3i2.ecommerce_backoffice.domain.product.dto;

import lombok.Getter;

@Getter
public class UpdateQuantityProductRequest {
    private Long id;
    private Long quantity;
}
