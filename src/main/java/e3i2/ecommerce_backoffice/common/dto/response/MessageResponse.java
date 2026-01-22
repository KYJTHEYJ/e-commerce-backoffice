package e3i2.ecommerce_backoffice.common.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({ "success", "code", "message" })
public class MessageResponse extends BaseResponse {
    private boolean success;
    private String code;
    private String message;

    public static MessageResponse success(String code, String message) {
        MessageResponse response = new MessageResponse();

        response.success = true;
        response.code = code;
        response.message = message;

        return response;
    }

    public static <T> MessageResponse fail(String code, String message) {
        MessageResponse response = new MessageResponse();

        response.success = false;
        response.code = code;
        response.message = message;

        return response;
    }
}
