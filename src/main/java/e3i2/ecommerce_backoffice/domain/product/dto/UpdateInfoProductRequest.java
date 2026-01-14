package e3i2.ecommerce_backoffice.domain.product.dto;

import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UpdateInfoProductRequest {
    private String productName;
    private ProductCategory category;
    private Long price;
}
