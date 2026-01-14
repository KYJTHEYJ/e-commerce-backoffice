package e3i2.ecommerce_backoffice.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
{
    "success": true,
    "code": "CREATED",
    "data": {
        "name": "테스트 상품",
        "category": "ELECTRONICS",
        "price": 90000,
        "stock": 10,
        "status": "AVAILABLE",
        "id": "P101",
        "createdAt": "2026-01-14",
        "createdBy": "0",
        "createdByName": "admin",
        "createdByEmail": "admin@sparta.com"
    }
}
*/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateProductResponse {
    private Long id;
    private String name;
    private String category;
    private Long price;
    private Long quantity;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    //private Long admin_id;
    //private String adminName;
    //private String adminEmail;

    public static CreateProductResponse regist(Long id, String name, String category, Long price, Long quantity, String status, LocalDateTime createdAt
            /*, Long adminId, String adminName, String adminEmail*/) {
        CreateProductResponse response = new CreateProductResponse();
        response.id = id;
        response.name = name;
        response.category = category;
        response.price = price;
        response.quantity = quantity;
        response.status = status;
        response.createdAt = createdAt;
        //response.admin_id = adminId;
        //response.adminName = adminName;
        //response.adminEmail = adminEmail;

        return response;
    }
}
