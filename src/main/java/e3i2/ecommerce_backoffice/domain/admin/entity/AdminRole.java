package e3i2.ecommerce_backoffice.domain.admin.entity;

import lombok.Getter;

@Getter
public enum AdminRole {
    SUPER_ADMIN("슈퍼 관리자")
    , CS_ADMIN("고객 관리자")
    , OP_ADMIN("운영 관리자");

    private final String roleDescription;

    AdminRole(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
