package e3i2.ecommerce_backoffice.domain.customer.entity;

import lombok.Getter;

@Getter
public enum CustomerStatus {
    ACT("활성")
    , IN_ACT("비활성")
    , SUSPEND("정지");

    private final String statusDescription;

    CustomerStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

}
