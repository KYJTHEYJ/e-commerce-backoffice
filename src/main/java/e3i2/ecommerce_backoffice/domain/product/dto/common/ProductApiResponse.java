package e3i2.ecommerce_backoffice.domain.product.dto.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductApiResponse<T> {
    public Boolean success;
    public String code;
    public T data;

    private static <T> ProductApiResponse<T> register(Boolean success, String code, T data) {
        ProductApiResponse<T> response = new ProductApiResponse<>();
        response.success = success;
        response.code = code;
        response.data = data;

        return response;
    }

    public static <T> ProductApiResponse<T> created(T data) {
        return register(true, HttpStatus.CREATED.name(), data);
    }

    public static <T> ProductApiResponse<T> ok(T data) {
        return register(true, HttpStatus.OK.name(), data);
    }
}
