package e3i2.ecommerce_backoffice.domain.admin.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum AdminRole {
    SUPER_ADMIN("SUPER_ADMIN", "슈퍼 관리자")
    , CS_ADMIN("CS_ADMIN", "고객 관리자")
    , OP_ADMIN("OP_ADMIN", "운영 관리자")
    , @JsonEnumDefaultValue UNKNOWN("UNKNOWN", "알수 없음");

    private final String roleCode;
    private final String roleDescription;

    AdminRole(String roleCode, String roleDescription) {
        this.roleCode = roleCode;
        this.roleDescription = roleDescription;
    }
}
