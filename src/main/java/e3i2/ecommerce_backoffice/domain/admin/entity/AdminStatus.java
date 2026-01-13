package e3i2.ecommerce_backoffice.domain.admin.entity;

import lombok.Getter;

@Getter
public enum AdminStatus {
    ACT("활성")
    , IN_ACT("비활성")
    , SUSPEND("정지")
    , WAIT("승인 대기")
    , DENY("거부");

    private final String statusDescription;

    AdminStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

}
