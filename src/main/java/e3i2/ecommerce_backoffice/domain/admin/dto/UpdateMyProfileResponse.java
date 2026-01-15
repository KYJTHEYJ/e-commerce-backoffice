package e3i2.ecommerce_backoffice.domain.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMyProfileResponse {

    private String adminName;
    private String email;
    private String phone;

    public static UpdateMyProfileResponse regist(
            String adminName,
            String email,
            String phone
    ) {
        UpdateMyProfileResponse response = new UpdateMyProfileResponse();
        response.adminName = adminName;
        response.email = email;
        response.phone = phone;
        return response;
    }
}

