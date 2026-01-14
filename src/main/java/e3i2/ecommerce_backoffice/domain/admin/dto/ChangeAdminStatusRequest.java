package e3i2.ecommerce_backoffice.domain.admin.dto;

import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeAdminStatusRequest {
    @NotBlank
    private AdminStatus status;
}