package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "category"
        , "count"
})
public class ProductCategoryCountResponse {
    private final String category;
    private final Long count;

    private ProductCategoryCountResponse(String category, Long count) {
        this.category = category;
        this.count = count;
    }

    public static ProductCategoryCountResponse register(String category, Long count) {
        return new ProductCategoryCountResponse(category, count);
    }
}
