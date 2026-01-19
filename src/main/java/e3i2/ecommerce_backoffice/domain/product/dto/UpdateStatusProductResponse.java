package e3i2.ecommerce_backoffice.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "id",
        "productName",
        "category",
        "price",
        "quantity",
        "status",
        "createdAt",
        "adminId",
        "adminName",
        "adminEmail"
})
public class UpdateStatusProductResponse {
    private final Long id;
    private final String productName;
    private final String category;
    private final Long price;
    private final Long quantity;
    private final String status;
    private final LocalDateTime createdAt;
    private final Long adminId;
    private final String adminName;
    private final String adminEmail;

    private UpdateStatusProductResponse(Long id, String productName, String category, Long price, Long quantity, String status, LocalDateTime createdAt
            , Long adminId, String adminName, String adminEmail) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public static UpdateStatusProductResponse register(Long id, String productName, ProductCategory category, Long price, Long quantity, ProductStatus status, LocalDateTime createdAt
            , Long adminId, String adminName, String adminEmail) {
        return new UpdateStatusProductResponse(id, productName, category.getCategoryCode(), price, quantity, status.getStatusCode(), createdAt, adminId, adminName, adminEmail);
    }
}
