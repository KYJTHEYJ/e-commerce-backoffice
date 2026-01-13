package e3i2.ecommerce_backoffice.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ON_SALE("판매중")
    , SOLD_OUT("품절")
    , DISCONTINUE("단종");

    private final String statusDescription;

    ProductStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

}
