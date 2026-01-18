package e3i2.ecommerce_backoffice.domain.product.repository.projection;

import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;

public interface ProductCategoryCount {
    ProductCategory getProductCategory();
    Long getProductCount();
}
