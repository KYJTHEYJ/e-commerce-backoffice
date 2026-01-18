package e3i2.ecommerce_backoffice.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({
        "id",
        "orderId",
        "productId",
        "customerId",
        "customer",
        "customerEmail",
        "product",
        "rating",
        "content",
        "createdAt"
})
public class SearchReviewResponse {
    @JsonProperty("id")
    private Long reviewId;

    @JsonProperty("orderId")
    private String orderNo;
    private Long productId;
    private Long customerId;

    @JsonProperty("customer")
    private String customerName;

    private String customerEmail;

    @JsonProperty("product")
    private String productName;

    private Integer rating;

    @JsonProperty("comment")
    private String content;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public static SearchReviewResponse register(
            Long reviewId,
            String orderNo,
            Long productId,
            Long customerId,
            String customerName,
            String customerEmail,
            String productName,
            Integer rating,
            String content,
            LocalDateTime createdAt
    ) {
        SearchReviewResponse response = new SearchReviewResponse();
        response.reviewId = reviewId;
        response.orderNo = orderNo;
        response.productId = productId;
        response.customerId = customerId;
        response.customerName = customerName;
        response.customerEmail = customerEmail;
        response.productName = productName;
        response.rating = rating;
        response.content = content;
        response.createdAt = createdAt;
        return response;
    }
}
