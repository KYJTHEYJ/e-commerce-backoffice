package e3i2.ecommerce_backoffice.domain.admin.dto.common;

import lombok.Getter;
// 응답 형식 success, code, message, data
// 메세지, 데이터 둘다 컬럼 출력되는 경우가 있었는지 확인 요청
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
