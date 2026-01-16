package e3i2.ecommerce_backoffice.domain.review.service;

import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.domain.review.dto.GetReviewResponse;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepositorty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepositorty reviewRepositorty;

    @Transactional(readOnly = true)
    public GetReviewResponse findOne(Long reviewId) {
        Review review = reviewRepositorty.findById(reviewId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_REVIEW)
        );
        return new GetReviewResponse(
                review.getReviewId(),
                review.getOrder().getOrderId(),
                review.getProduct().getProductId(),
                review.getCustomer().getCustomerId(),
                review.getCustomer().getCustomerName(),
                review.getCustomer().getEmail(),
                review.getProduct().getProductName(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }

    @Transactional
    public void delete(Long reviewId) {
        Review review = reviewRepositorty.findByReviewIdAndDeletedFalse(reviewId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_REVIEW)
        );
        review.delete();
    }
}
