package e3i2.ecommerce_backoffice.domain.customer.dto;

import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import lombok.Getter;

@Getter
public class PatchStatusCustomerRequest {
    private CustomerStatus status;
}
