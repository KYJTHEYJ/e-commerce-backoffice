package e3i2.ecommerce_backoffice.domain.admin.dto;

import lombok.Getter;

// 생성자 정적 팩토리 메서드로 권장
@Getter
public class GetMyProfileResponse {
    private final String adminName;
    private final String email;
    private final String phone;

    public GetMyProfileResponse(String adminName, String email, String phone) {
        this.adminName = adminName;
        this.email = email;
        this.phone = phone;
    }
}
