package e3i2.ecommerce_backoffice.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum ProductCategory {
    ELECTRONICS("ELECTRONICS", "전자")
    , FOOD("FOOD", "음식")
    , CLOTH("CLOTH","의류")
    , @JsonEnumDefaultValue UNKNOWN("UNKNOWN", "알수 없음");

    private final String categoryCode;
    private final String categoryDescription;

    ProductCategory(String categoryCode, String categoryDescription) {
        this.categoryCode = categoryCode;
        this.categoryDescription = categoryDescription;
    }
}
