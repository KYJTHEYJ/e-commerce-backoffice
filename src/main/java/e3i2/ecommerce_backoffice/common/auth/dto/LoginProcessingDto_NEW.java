package e3i2.ecommerce_backoffice.common.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_EMAIL_PATTERN_ERR;
import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_PASSWORD_SIZE_ERR;

public class LoginProcessingDto_NEW {
    @Email(message = MSG_EMAIL_PATTERN_ERR)
    private String email;

    @Size(min = 8, max = 20, message = MSG_PASSWORD_SIZE_ERR)
    private String password;
}
