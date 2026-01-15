package e3i2.ecommerce_backoffice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    NOT_ADMIN_STATUS_WAIT(HttpStatus.CONFLICT, "해당 계정은 승인 대기 상태가 아닙니다")
    ,LOGOUT_DUPLICATED(HttpStatus.UNAUTHORIZED, "정상적인 접근이 아닙니다");

    private final HttpStatus status;
    private final String message;

    ErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;

    }
}
