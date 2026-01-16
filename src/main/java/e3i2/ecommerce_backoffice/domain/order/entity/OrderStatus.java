package e3i2.ecommerce_backoffice.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
     CANCELLED("CANCELLED", "취소"),
    PREPARING("PREPARING", "준비중"),
    SHIPPING("SHIPPING", "배송중"),
    DELIVERED("DELIVERED", "배송완료");

    private final String statusCode;
    private final String statusDescription;

    OrderStatus(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }
}
