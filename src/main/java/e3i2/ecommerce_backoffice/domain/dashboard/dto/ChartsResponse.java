package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({
        "reviewRating"
        , "customerStatus"
        , "productCategory"
})
public class ChartsResponse {
    private final List<ReviewRatingCountResponse> reviewRating;
    private final List<CustomerStatusCountResponse> customerStatus;
    private final List<ProductCategoryCountResponse> productCategory;

    private ChartsResponse(
            List<ReviewRatingCountResponse> reviewRating
            , List<CustomerStatusCountResponse> customerStatus
            , List<ProductCategoryCountResponse> productCategory
    ) {
        this.reviewRating = reviewRating;
        this.customerStatus = customerStatus;
        this.productCategory = productCategory;
    }

    public static ChartsResponse register(
            List<ReviewRatingCountResponse> reviewRating
            , List<CustomerStatusCountResponse> customerStatus
            , List<ProductCategoryCountResponse> productCategory
    ) {
        return new ChartsResponse(
                reviewRating
                , customerStatus
                , productCategory
        );
    }
}
