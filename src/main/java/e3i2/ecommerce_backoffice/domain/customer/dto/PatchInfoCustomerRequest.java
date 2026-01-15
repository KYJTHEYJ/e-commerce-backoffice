package e3i2.ecommerce_backoffice.domain.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PatchInfoCustomerRequest {
    @NotBlank
    private String customerName;
    @Email
    private String email;
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phone;
}
