package e3i2.ecommerce_backoffice.domain.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//TODO admin 기능 구현시, 주석 단위 교체 필요
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateQuantityProductResponse {
    private Long id;
    private String productName;
    private String category;
    private Long price;
    private Long quantity;
    private String status;
    private LocalDateTime createdAt;
    //private Long adminId;
    //private String adminName;
    //private String adminEmail;

    public static UpdateQuantityProductResponse regist(Long id, String productName, String category, Long price, Long quantity, String status, LocalDateTime createdAt) {
        UpdateQuantityProductResponse response = new UpdateQuantityProductResponse();
        response.id = id;
        response.productName = productName;
        response.category = category;
        response.price = price;
        response.quantity = quantity;
        response.status = status;
        response.createdAt = createdAt;

        return response;
    }
}
