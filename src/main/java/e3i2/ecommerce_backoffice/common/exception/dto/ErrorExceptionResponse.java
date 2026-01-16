package e3i2.ecommerce_backoffice.common.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorExceptionResponse {
    private HttpStatus httpStatus;
    private String message;

    public static ErrorExceptionResponse register(HttpStatus httpStatus, String message) {
        ErrorExceptionResponse response = new ErrorExceptionResponse();
        response.httpStatus = httpStatus;
        response.message = message;

        return response;
    }
}
