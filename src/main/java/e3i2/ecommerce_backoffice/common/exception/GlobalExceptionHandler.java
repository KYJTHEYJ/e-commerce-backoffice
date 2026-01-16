package e3i2.ecommerce_backoffice.common.exception;

import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static e3i2.ecommerce_backoffice.common.util.Constants.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler<T> {
    @ExceptionHandler(ServiceErrorException.class)
    public ResponseEntity<MessageResponse<T>> handleServiceErrorException(ServiceErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(MessageResponse.fail(e.getHttpStatus().name(), e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse<T>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("데이터 유효성 에러 발생 : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.fail(HttpStatus.BAD_REQUEST.name(), e.getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<MessageResponse<T>> HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("데이터 JSON 변환 에러 발생 : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.fail(HttpStatus.BAD_REQUEST.name(), MSG_NOT_VALID_VALUE));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<MessageResponse<T>> DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        log.error("데이터 등록 실패 발생 : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.fail(HttpStatus.BAD_REQUEST.name(), MSG_DATA_INSERT_FAIL));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse<T>> handleCriticalErrorException(Exception e) {
        log.error("서버 에러 발생", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), MSG_SERVER_ERROR_OCCUR));
    }
}
