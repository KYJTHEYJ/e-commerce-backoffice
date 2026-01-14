package e3i2.ecommerce_backoffice.domain.admin.dto;

import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class SignupRequest {
    @NotBlank(message = "이름은 필수값입니다.")
    private String adminName;

    @Email(message = "이메일 형식으로 작성해야 합니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자리 이상이여야 합니다.")
    private String password;

    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호는 010-xxxx-xxxx 형식이어야 합니다."
    )
    private String phone;

    @NotNull(message = "관리자 역할은 필수값입니다.")
    private AdminRole role;
}
