package e3i2.ecommerce_backoffice.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "averageRating"
        , "totalReviews"
        , "fiveStarCount"
        , "fourStarCount"
        , "threeStarCount"
        , "twoStarCount"
        , "oneStarCount"
})
public class SearchReviewSummaryResponse {
    private final Double averageRating;
    private final Integer totalReviews;
    private final Integer fiveStarCount;
    private final Integer fourStarCount;
    private final Integer threeStarCount;
    private final Integer twoStarCount;
    private final Integer oneStarCount;

    private SearchReviewSummaryResponse(
            Double averageRating
            , Integer totalReviews
            , Integer fiveStarCount
            , Integer fourStarCount
            , Integer threeStarCount
            , Integer twoStarCount
            , Integer oneStarCount) {
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.fiveStarCount = fiveStarCount;
        this.fourStarCount = fourStarCount;
        this.threeStarCount = threeStarCount;
        this.twoStarCount = twoStarCount;
        this.oneStarCount = oneStarCount;
    }

    public static SearchReviewSummaryResponse register(
            Double averageRating
            , Integer totalReviews
            , Integer fiveStarCount
            , Integer fourStarCount
            , Integer threeStarCount
            , Integer twoStarCount
            , Integer oneStarCount
    ) {
        return new SearchReviewSummaryResponse(
                averageRating
                , totalReviews
                , fiveStarCount
                , fourStarCount
                , threeStarCount
                , twoStarCount
                , oneStarCount
        );
    }
}
