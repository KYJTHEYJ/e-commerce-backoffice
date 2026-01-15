package e3i2.ecommerce_backoffice.domain.customer.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCustomerResponse {
    private final Long customerId;
    private final String customerName;
    private final String email;
    private final String phone;
    private final String status;
    private final LocalDateTime createdAt;

    public GetCustomerResponse(Long customerId, String customerName, String email, String phone, String status, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
    }
}
