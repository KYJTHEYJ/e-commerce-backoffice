package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }
}
