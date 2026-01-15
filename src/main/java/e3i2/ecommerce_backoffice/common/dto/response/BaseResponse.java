package e3i2.ecommerce_backoffice.common.dto.response;

import lombok.Getter;

@Getter
public abstract class BaseResponse {
    protected boolean success;
    protected String code;
}
