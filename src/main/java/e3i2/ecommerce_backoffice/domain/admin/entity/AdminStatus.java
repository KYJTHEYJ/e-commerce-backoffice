package e3i2.ecommerce_backoffice.domain.admin.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum AdminStatus {
    ACT("ACT", "활성")
    , IN_ACT("IN_ACT", "비활성")
    , SUSPEND("SUSPEND", "정지")
    , WAIT("WAIT", "승인 대기")
    , DENY("DENY", "거부")
    , @JsonEnumDefaultValue UNKNOWN("UNKNOWN", "알수 없음");

    private final String statusCode;
    private final String statusDescription;

    AdminStatus(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

}
