package e3i2.ecommerce_backoffice.domain.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
    @Email(message = "이메일 형식으로 작성해야 합니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자리 이상이여야 합니다.")
    private String password;
}
