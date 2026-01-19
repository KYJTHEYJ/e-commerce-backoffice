package e3i2.ecommerce_backoffice.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "id",
        "orderNo",
        "customerId",
        "customerName",
        "productId",
        "productName",
        "quantity",
        "amount",
        "orderAt",
        "status",
        "AdminId",
        "AdminName",
        "AdminRole"
})
public class SearchOrderingResponse {
    private final Long id;
    private final String orderNo;
    private final Long customerId;
    private final String customerName;
    private final Long productId;
    private final String productName;
    private final Long quantity;
    private final Long amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime orderAt;
    private final String orderStatus;
    private final Long AdminId;
    private final String AdminName;
    private final String AdminRole;

    private SearchOrderingResponse(
            Long id, String orderNo, Long customerId, String customerName, Long productId, String productName,
            Long quantity, Long amount, LocalDateTime orderAt, String orderStatus,
            Long AdminId, String AdminName, String AdminRole) {
        this.id = id;
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.AdminId = AdminId;
        this.AdminName = AdminName;
        this.AdminRole = AdminRole;
    }

    public static SearchOrderingResponse register(
            Long id, String orderNo, Long customerId, String customerName, Long productId, String productName,
            Long quantity, Long amount, LocalDateTime orderAt, OrderingStatus orderStatus,
            Long AdminId, String AdminName, AdminRole AdminRole) {
        return new SearchOrderingResponse(id, orderNo, customerId, customerName, productId, productName, quantity, amount, orderAt, orderStatus.getStatusCode(), AdminId, AdminName, AdminRole.getRoleCode());
    }

}
