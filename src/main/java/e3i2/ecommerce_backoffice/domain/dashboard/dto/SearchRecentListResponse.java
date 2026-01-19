package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
    "orderId",
    "orderNo",
    "customerId",
    "customerName",
    "email",
    "productName",
    "orderQuantity",
    "orderTotalPrice",
    "orderCreatedAt",
    "status"
})
public class SearchRecentListResponse {
    private final Long orderId;
    private final String orderNo;
    private final Long customerId;
    private final String customerName;
    private final String email;
    private final String productName;
    private final Long orderQuantity;
    private final Long orderTotalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime orderCreatedAt;
    private final String status;

    private SearchRecentListResponse(
            Long orderId,
            String orderNo,
            Long customerId,
            String customerName,
            String email,
            String productName,
            Long orderQuantity,
            Long orderTotalPrice,
            LocalDateTime orderCreatedAt,
            String status

    ) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.orderTotalPrice = orderTotalPrice;
        this.orderCreatedAt = orderCreatedAt;
        this.status = status;
    }

    public static SearchRecentListResponse register(
            Long orderId,
            String orderNo,
            Long customerId,
            String customerName,
            String email,
            String productName,
            Long orderQuantity,
            Long orderTotalPrice,
            LocalDateTime orderCreatedAt,
            OrderingStatus status

    ) {
        return new SearchRecentListResponse(orderId, orderNo, customerId, customerName, email, productName, orderQuantity, orderTotalPrice, orderCreatedAt, status.getStatusCode());
    }


}
