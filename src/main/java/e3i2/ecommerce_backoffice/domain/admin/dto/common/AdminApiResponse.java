package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import lombok.Getter;

@Getter
public class AdminApiResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private AdminApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> AdminApiResponse<T> success(String code, String message, T data) {
        return new AdminApiResponse<>(true, code, message, data);
    }
}
