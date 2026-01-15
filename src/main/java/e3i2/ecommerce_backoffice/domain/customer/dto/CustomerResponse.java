package e3i2.ecommerce_backoffice.domain.customer.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "code", "data"})
public class CustomerResponse<T> {
    public Boolean success;
    public String code;
    public T data;

    public static <T> CustomerResponse<T> regist(Boolean success, String code, T data) {
        CustomerResponse<T> response = new CustomerResponse<>();
        response.success = success;
        response.code = code;
        response.data = data;

        return response;
    }
}
