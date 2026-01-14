package e3i2.ecommerce_backoffice.domain.product.dto.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductApiResponse2 {
    public Boolean success;
    public String code;
    public String message;

    private static ProductApiResponse2 regist(Boolean success, String code, String message) {
        ProductApiResponse2 response = new ProductApiResponse2();
        response.success = success;
        response.code = code;
        response.message = message;

        return response;
    }

    public static ProductApiResponse2 deleted() {
        return regist(true, HttpStatus.OK.name(), "상품이 삭제되었습니다");
    }
}
