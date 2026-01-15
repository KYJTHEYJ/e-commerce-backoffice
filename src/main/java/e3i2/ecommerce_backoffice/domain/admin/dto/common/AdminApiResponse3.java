package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import lombok.Getter;
// 응답 형식 success, code, message
@Getter
public class AdminApiResponse3 {
    private final boolean success;
    private final String code;
    private final String message;

    private AdminApiResponse3(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static AdminApiResponse3 success(String code, String message) {
        return new AdminApiResponse3(true, code, message);
    }
}
