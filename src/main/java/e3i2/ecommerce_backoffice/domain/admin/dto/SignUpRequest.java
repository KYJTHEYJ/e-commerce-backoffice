package e3i2.ecommerce_backoffice.domain.admin.dto;

import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import jakarta.validation.constraints.*;
import lombok.Getter;

import static e3i2.ecommerce_backoffice.common.util.Constants.*;

@Getter
public class SignUpRequest {
    @NotBlank(message = MSG_NAME_BLANK_ERR)
    private String adminName;

    @Email(message = MSG_EMAIL_PATTERN_ERR)
    private String email;

    @Size(min = 8, max = 20, message = MSG_PASSWORD_SIZE_ERR)
    private String password;

    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = MSG_PHONE_PATTERN_ERR
    )
    private String phone;

    @NotNull(message = MSG_ADMIN_ACCOUNT_ROLE_NULL_ERR)
    private AdminRole role;

    @NotBlank(message = MSG_ADMIN_REQUEST_MESSAGE_BLANK_ERR)
    private String requestMessage;
}
