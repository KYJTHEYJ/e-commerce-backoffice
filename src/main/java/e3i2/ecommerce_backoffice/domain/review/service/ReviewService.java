package e3i2.ecommerce_backoffice.domain.review.service;

import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.review.dto.GetReviewResponse;
import e3i2.ecommerce_backoffice.domain.review.dto.SearchReviewListResponse;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepositorty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepositorty reviewRepositorty;

    // 리뷰 리스트 조회
    @Transactional(readOnly = true)
    public ItemsWithPagination<List<SearchReviewListResponse>> getReviewList(String keyword, Integer page, Integer size, String sortBy, String sortOrder, @Min(1) @Max(5) Integer rating) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        String safeKeyword = (keyword == null || keyword.isBlank()) ? null : keyword;

        Page<Review> reviews = reviewRepositorty.findReviews(safeKeyword, rating, pageable);

        List<SearchReviewListResponse> items = reviews.getContent().stream()
                .map(r -> SearchReviewListResponse.register(
                        r.getReviewId(),
                        Long.valueOf(r.getOrder().getOrderNo()), // 주문번호(orderNo)
                        r.getProduct().getProductId(),
                        r.getCustomer().getCustomerId(),
                        r.getCustomer().getCustomerName(),
                        r.getCustomer().getEmail(),
                        r.getProduct().getProductName(),
                        r.getRating(),
                        r.getContent(),
                        r.getCreatedAt()                // date
                ))
                .toList();

        return ItemsWithPagination.register(items, page, size, reviews.getTotalElements());
    }

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
