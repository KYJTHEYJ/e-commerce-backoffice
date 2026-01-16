package e3i2.ecommerce_backoffice.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"reviewId", "orderId", "productId", "customerId", "customerName", "customerEmail", "productName", "rating", "content", "createdAt"})
public class GetReviewResponse {
    private final Long reviewId;
    private final Long orderId;
    private final Long productId;
    private final Long customerId;
    private final String customerName;
    private final String customerEmail;
    private final String productName;
    private final Integer rating;
    private final String content;
    private final LocalDateTime createdAt;

    public GetReviewResponse(Long reviewId, Long orderId, Long productId, Long customerId, String customerName, String customerEmail, String productName, Integer rating, String content, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.productId = productId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }
}
