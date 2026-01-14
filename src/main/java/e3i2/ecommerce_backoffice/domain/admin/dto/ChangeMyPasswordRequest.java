package e3i2.ecommerce_backoffice.domain.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangeMyPasswordRequest {
    @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
    private String currentPassword;
    @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
    private String newPassword;
}
