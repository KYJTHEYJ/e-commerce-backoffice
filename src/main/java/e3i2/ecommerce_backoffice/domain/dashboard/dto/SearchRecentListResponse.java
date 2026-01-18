package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRecentListResponse {
    private Long orderId;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private String email;
    private String productName;
    private Long orderQuantity;
    private Long orderTotalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime orderCreatedAt;
    private OrderingStatus status;

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
        SearchRecentListResponse response = new SearchRecentListResponse();
        response.orderId = orderId;
        response.orderNo = orderNo;
        response.customerId = customerId;
        response.customerName = customerName;
        response.email = email;
        response.productName = productName;
        response.orderQuantity = orderQuantity;
        response.orderTotalPrice = orderTotalPrice;
        response.orderCreatedAt = orderCreatedAt;
        response.status = status;

        return response;
    }


}
