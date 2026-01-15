package e3i2.ecommerce_backoffice.domain.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateMyProfileRequest {
    @NotBlank(message = "이름은 필수값입니다.")
    private String adminName;
    @Email(message = "이메일 형식으로 작성해야 합니다.")
    private String email;
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호는 010-xxxx-xxxx 형식이어야 합니다."
    )
    private String phone;
}
