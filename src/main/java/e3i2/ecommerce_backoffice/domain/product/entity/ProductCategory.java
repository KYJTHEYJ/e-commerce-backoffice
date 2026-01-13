package e3i2.ecommerce_backoffice.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductCategory {
    ELECTRONICS("전자")
    , FOOD("음식")
    , CLOTH("의류");

    private final String categoryDescription;

    ProductCategory(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
