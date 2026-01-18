package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "rating"
        , "count"
})
public class ReviewRatingCountResponse {
    private final Integer rating;
    private final Long count;

    private ReviewRatingCountResponse(Integer rating, Long count) {
        this.rating = rating;
        this.count = count;
    }

    public static ReviewRatingCountResponse register(Integer rating, Long count) {
        return new ReviewRatingCountResponse(rating, count);
    }
}
