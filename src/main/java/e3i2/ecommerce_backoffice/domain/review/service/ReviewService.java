package e3i2.ecommerce_backoffice.domain.review.service;

import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.review.dto.SearchReviewResponse;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepository;
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
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public ItemsWithPagination<List<SearchReviewResponse>> getReviewList(String search, Integer page, Integer size, String sortBy, String sortOrder, @Min(1) @Max(5) Integer rating) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        search = (search == null || search.isBlank()) ? null : search;

        Page<Review> reviews = reviewRepository.findReviews(search, rating, pageable);

        List<SearchReviewResponse> items = reviews.getContent().stream()
                .map(r -> SearchReviewResponse.register(
                        r.getReviewId(),
                        r.getOrder().getOrderNo(),
                        r.getProduct().getProductId(),
                        r.getCustomer().getCustomerId(),
                        r.getCustomer().getCustomerName(),
                        r.getCustomer().getEmail(),
                        r.getProduct().getProductName(),
                        r.getRating(),
                        r.getContent(),
                        r.getCreatedAt()
                ))
                .toList();

        return ItemsWithPagination.register(items, page, size, reviews.getTotalElements());
    }

    @Transactional(readOnly = true)
    public SearchReviewResponse findOne(Long reviewId) {
        Review review = reviewRepository.findByReviewIdAndDeletedFalse(reviewId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_REVIEW)
        );
        return SearchReviewResponse.register(
                review.getReviewId(),
                review.getOrder().getOrderNo(),
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
        Review review = reviewRepository.findByReviewIdAndDeletedFalse(reviewId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_REVIEW)
        );
        review.delete();
    }
}
