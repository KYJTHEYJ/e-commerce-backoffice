package e3i2.ecommerce_backoffice.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum OrderingStatus {
    CANCELLED("CANCELLED", "취소"),
    PREPARING("PREPARING", "준비중"),
    SHIPPING("SHIPPING", "배송중"),
    DELIVERED("DELIVERED", "배송완료")
    , @JsonEnumDefaultValue UNKNOWN("UNKNOWN", "알수 없음");

    private final String statusCode;
    private final String statusDescription;

    OrderingStatus(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }
}
