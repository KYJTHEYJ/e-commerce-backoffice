package e3i2.ecommerce_backoffice.domain.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStatusProductResponse {
    private Long id;
    private String productName;
    private String category;
    private Long price;
    private Long quantity;
    private String status;
    private LocalDateTime createdAt;
    private Long adminId;
    private String adminName;
    private String adminEmail;

    public static UpdateStatusProductResponse regist(Long id, String productName, String category, Long price, Long quantity, String status, LocalDateTime createdAt
            , Long adminId, String adminName, String adminEmail) {
        UpdateStatusProductResponse response = new UpdateStatusProductResponse();
        response.id = id;
        response.productName = productName;
        response.category = category;
        response.price = price;
        response.quantity = quantity;
        response.status = status;
        response.createdAt = createdAt;
        response.adminId = adminId;
        response.adminName = adminName;
        response.adminEmail = adminEmail;

        return response;
    }
}
