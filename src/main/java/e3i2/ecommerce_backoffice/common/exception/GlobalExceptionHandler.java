package e3i2.ecommerce_backoffice.common.exception;

import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static e3i2.ecommerce_backoffice.common.util.Constants.SERVER_ERROR_OCCUR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler<T> {
    @ExceptionHandler(ServiceErrorException.class)
    public MessageResponse<T> handleServiceErrorException(ServiceErrorException e) {
        log.error(e.getMessage(), e);
        return MessageResponse.fail(e.getHttpStatus().name(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public MessageResponse<T> handleCriticalErrorException(Exception e) {
        log.error("서버 에러 발생", e);
        return MessageResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), SERVER_ERROR_OCCUR);
    }
}
