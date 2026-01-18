package e3i2.ecommerce_backoffice.domain.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
public class PutStatusCustomerResponse {
    private final Long customerId;
    private final String customerName;
    private final String email;
    private final String phone;
    private final String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;
    private final Long totalOrders;
    private final Long totalSpent;

    public PutStatusCustomerResponse(Long customerId, String customerName, String email, String phone, String status, LocalDateTime createdAt, Long totalOrders, Long totalSpent) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
    }
}
