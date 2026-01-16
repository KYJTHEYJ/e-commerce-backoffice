package e3i2.ecommerce_backoffice.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "orderId"
        , "orderNo"
        , "orderAt"
        , "orderStatus"
        , "customerId"
        , "customerName"
        , "productId"
        , "productName"
        , "productPrice"
        , "orderQuantity"
        , "orderTotalPrice"
        , "adminId"
        , "adminName"
        , "adminEmail"
})
public class CreateOrderingResponse {
    private final Long orderId;
    private final String orderNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime orderAt;
    private final String orderStatus;
    private final Long customerId;
    private final String customerName;
    private final Long productId;
    private final String productName;
    private final Long productPrice;
    private final Long orderQuantity;
    private final Long orderTotalPrice;
    private final Long adminId;
    private final String adminName;
    private final String adminEmail;

    private CreateOrderingResponse(
            Long orderId
            , String orderNo
            , LocalDateTime orderAt
            , String orderStatus
            , Long customerId
            , String customerName
            , Long productId
            , String productName
            , Long productPrice
            , Long orderQuantity
            , Long orderTotalPrice
            , Long adminId
            , String adminName
            , String adminEmail
    ) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderQuantity = orderQuantity;
        this.orderTotalPrice = orderTotalPrice;
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public static CreateOrderingResponse register(
            Long orderId
            , String orderNo
            , LocalDateTime orderAt
            , String orderStatus
            , Long customerId
            , String customerName
            , Long productId
            , String productName
            , Long productPrice
            , Long orderQuantity
            , Long orderTotalPrice
            , Long adminId
            , String adminName
            , String adminEmail
    ) {
        return new CreateOrderingResponse(
                orderId
                , orderNo
                , orderAt
                , orderStatus
                , customerId
                , customerName
                , productId
                , productName
                , productPrice
                , orderQuantity
                , orderTotalPrice
                , adminId
                , adminName
                , adminEmail
        );
    }
}
