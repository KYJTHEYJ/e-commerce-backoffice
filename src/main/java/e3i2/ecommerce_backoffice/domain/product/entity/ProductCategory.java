package e3i2.ecommerce_backoffice.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductCategory {
    ELECTRONICS("ELECTRONICS", "전자")
    , FOOD("FOOD", "음식")
    , CLOTH("CLOTH","의류");

    private final String categoryCode;
    private final String categoryDescription;

    ProductCategory(String categoryCode, String categoryDescription) {
        this.categoryCode = categoryCode;
        this.categoryDescription = categoryDescription;
    }
}
