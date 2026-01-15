package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import lombok.Getter;
// 응답 형식 success, code, data
@Getter
public class AdminApiResponse2<T> {
    private final boolean success;
    private final String code;
    private final T data;

    private AdminApiResponse2(boolean success, String code, T data) {
        this.success = success;
        this.code = code;
        this.data = data;
    }

    public static <T> AdminApiResponse2<T> success(String code, T data) {
        return new AdminApiResponse2<>(true, code, data);
    }
}
