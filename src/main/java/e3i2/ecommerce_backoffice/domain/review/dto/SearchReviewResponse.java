package e3i2.ecommerce_backoffice.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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
    private final Long reviewId;

    private final String orderNo;
    private final Long productId;
    private final Long customerId;

    @JsonProperty("customer")
    private final String customerName;

    private final String customerEmail;

    @JsonProperty("product")
    private final String productName;

    private final Integer rating;

    @JsonProperty("comment")
    private final String content;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    private SearchReviewResponse(
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
        this.reviewId = reviewId;
        this.orderNo = orderNo;
        this.productId = productId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }

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
        return new SearchReviewResponse(
                reviewId,
                orderNo,
                productId,
                customerId,
                customerName,
                customerEmail,
                productName,
                rating,
                content,
                createdAt
        );
    }
}
