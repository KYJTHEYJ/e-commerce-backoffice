package e3i2.ecommerce_backoffice.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ON_SALE("ON_SALE","판매중")
    , SOLD_OUT("SOLD_OUT","품절")
    , DISCONTINUE("DISCONTINUE","단종");

    private final String statusCode;
    private final String statusDescription;

    ProductStatus(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

}
