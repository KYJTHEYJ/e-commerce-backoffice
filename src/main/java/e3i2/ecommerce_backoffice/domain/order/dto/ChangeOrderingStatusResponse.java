package e3i2.ecommerce_backoffice.domain.order.dto;

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
    "productId",
    "productName",
    "orderQuantity",
    "orderTotalPrice",
    "createdAt",
    "status"
})
public class ChangeOrderingStatusResponse {
    private final Long orderId;
    private final String orderNo;
    private final Long customerId;
    private final String customerName;
    private final String email;
    private final Long productId;
    private final String productName;
    private final Long orderQuantity;
    private final Long orderTotalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;
    private final String status;

    private ChangeOrderingStatusResponse(
        Long orderId,
        String orderNo,
        Long customerId,
        String customerName,
        String email,
        Long productId,
        String productName,
        Long orderQuantity,
        Long orderTotalPrice,
        LocalDateTime createdAt,
        String status
    ) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.productId = productId;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.orderTotalPrice = orderTotalPrice;
        this.createdAt = createdAt;
        this.status= status;
    }

    public static ChangeOrderingStatusResponse register(
        Long orderId,
        String orderNo,
        Long customerId,
        String customerName,
        String email,
        Long productId,
        String productName,
        Long orderQuantity,
        Long orderTotalPrice,
        LocalDateTime createdAt,
        OrderingStatus status
    ) {
        return new ChangeOrderingStatusResponse(orderId, orderNo, customerId, customerName, email, productId, productName, orderQuantity, orderTotalPrice, createdAt, status.getStatusCode());
    }

}
