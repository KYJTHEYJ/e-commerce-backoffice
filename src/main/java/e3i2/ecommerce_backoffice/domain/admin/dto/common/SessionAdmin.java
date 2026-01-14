package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import lombok.Getter;

@Getter
public class SessionAdmin {
    private final Long adminId;
    private final String email;
    private final String adminName;

    public SessionAdmin(Long adminId, String email, String adminName) {
        this.adminId = adminId;
        this.email = email;
        this.adminName = adminName;
    }
}
