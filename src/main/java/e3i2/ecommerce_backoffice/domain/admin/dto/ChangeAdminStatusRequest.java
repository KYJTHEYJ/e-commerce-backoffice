package e3i2.ecommerce_backoffice.domain.admin.dto;

import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_ADMIN_ACCOUNT_STATUS_NULL_ERR;

@Getter
public class ChangeAdminStatusRequest {
    @NotNull(message = MSG_ADMIN_ACCOUNT_STATUS_NULL_ERR)
    private AdminStatus status;
}