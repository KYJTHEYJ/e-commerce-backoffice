package e3i2.ecommerce_backoffice.domain.admin.dto;

import lombok.Getter;

@Getter
public class UpdateMyProfileResponse {
    private final String adminName;
    private final String email;
    private final String phone;

    public UpdateMyProfileResponse(String adminName, String email, String phone) {
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
    }
}
