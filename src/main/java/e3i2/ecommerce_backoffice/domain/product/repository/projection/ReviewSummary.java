package e3i2.ecommerce_backoffice.domain.product.repository.projection;

public interface ReviewSummary {
    Double getAverageRating();
    Integer getTotalReviews();
    Integer getFiveStarCount();
    Integer getFourStarCount();
    Integer getThreeStarCount();
    Integer getTwoStarCount();
    Integer getOneStarCount();
 }
