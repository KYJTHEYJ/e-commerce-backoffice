package e3i2.ecommerce_backoffice.domain.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetMyProfileResponse {

    private String adminName;
    private String email;
    private String phone;

    public static GetMyProfileResponse regist(
            String adminName,
            String email,
            String phone
    ) {
        GetMyProfileResponse response = new GetMyProfileResponse();
        response.adminName = adminName;
        response.email = email;
        response.phone = phone;
        return response;
    }
}

