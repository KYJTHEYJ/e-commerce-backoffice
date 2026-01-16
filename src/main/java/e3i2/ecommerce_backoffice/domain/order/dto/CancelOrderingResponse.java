package e3i2.ecommerce_backoffice.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CancelOrderingResponse {
    private Long orderId;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private String email;
    private Long productId;
    private String productName;
    private Long orderQuantity;
    private Long orderTotalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private OrderingStatus status;
    private String cancelReason;

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
        CancelOrderingResponse response = new CancelOrderingResponse();
        response.orderId = orderId;
        response.orderNo = orderNo;
        response.customerId = customerId;
        response.customerName = customerName;
        response.email = email;
        response.productId = productId;
        response.productName = productName;
        response.orderQuantity = orderQuantity;
        response.orderTotalPrice = orderTotalPrice;
        response.createdAt = deleteAt;
        response.status = status;
        response.cancelReason = cancelReason;
        return response;
    }
}
