package e3i2.ecommerce_backoffice.domain.product.dto;

import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateProductRequest {
    @NotBlank(message = "상품의 이름을 입력해주세요")
    private String name;
    @NotBlank(message = "상품의 카테고리를 입력해주세요")
    private ProductCategory category;
    @NotBlank(message = "상품의 가격을 입력해주세요")
    @Min(value = 0, message = "상품의 가격은 0원 이상이어야 합니다")
    @Max(value = Long.MAX_VALUE, message = "상품의 가격이 한도를 넘었습니다")
    private Long price;
    @NotBlank(message = "상품의 상태를 입력해주세요")
    private ProductStatus status;
    @NotBlank(message = "상품의 수량을 입력해주세요")
    @Max(value = Long.MAX_VALUE, message = "상품의 수량이 한도를 넘었습니다")
    private Long quantity;
}
