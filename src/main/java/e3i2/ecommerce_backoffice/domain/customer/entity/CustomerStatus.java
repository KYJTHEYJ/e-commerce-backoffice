package e3i2.ecommerce_backoffice.domain.customer.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum CustomerStatus {
    ACT("ACT","활성")
    , IN_ACT( "IN_ACT","비활성")
    , SUSPEND( "SUSPEND","정지")
    , @JsonEnumDefaultValue UNKNOWN("UNKNOWN", "알수 없음");

    private final String statusCode;
    private final String statusDescription;

    CustomerStatus(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

}
