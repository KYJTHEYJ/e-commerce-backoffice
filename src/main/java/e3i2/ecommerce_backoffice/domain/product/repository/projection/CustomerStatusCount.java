package e3i2.ecommerce_backoffice.domain.product.repository.projection;

import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;

public interface CustomerStatusCount {
    CustomerStatus getCustomerStatus();
    Long getCustomerCount();
}
