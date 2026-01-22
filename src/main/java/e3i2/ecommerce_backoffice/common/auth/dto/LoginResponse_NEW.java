package e3i2.ecommerce_backoffice.common.auth.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "token"
})
public record LoginResponse_NEW (
        String token
) {

}
