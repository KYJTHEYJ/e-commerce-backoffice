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
    "status",
    "cancelReason"
})
public class CancelOrderingResponse {
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
    private final String cancelReason;

    private CancelOrderingResponse(
            Long orderId,
            String orderNo,
            Long customerId,
            String customerName,
            String email,
            Long productId,
            String productName,
            Long orderQuantity,
            Long orderTotalPrice,
            LocalDateTime deleteAt,
            String status,
            String cancelReason
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
        this.createdAt = deleteAt;
        this.status = status;
        this.cancelReason = cancelReason;
    }

    public static CancelOrderingResponse register(
            Long orderId,
            String orderNo,
            Long customerId,
            String customerName,
            String email,
            Long productId,
            String productName,
            Long orderQuantity,
            Long orderTotalPrice,
            LocalDateTime deleteAt,
            OrderingStatus status,
            String cancelReason
    ) {
        return new CancelOrderingResponse(orderId, orderNo, customerId, customerName, email, productId, productName, orderQuantity, orderTotalPrice, deleteAt, status.getStatusCode(), cancelReason);
    }
}
