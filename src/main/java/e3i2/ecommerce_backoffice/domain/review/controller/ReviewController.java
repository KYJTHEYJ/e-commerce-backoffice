package e3i2.ecommerce_backoffice.domain.review.controller;

import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.domain.review.service.ReviewService;
import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.review.dto.SearchReviewResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_DELETE_REVIEW_ACCOUNT;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/api/reviews")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ItemsWithPagination<List<SearchReviewResponse>>>> getReviewList(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) @Min(1) @Max(5) Integer rating
    ) {
        ItemsWithPagination<List<SearchReviewResponse>> response = reviewService.getReviewList(
                search, page, size, sortBy, sortOrder, rating
        );

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @GetMapping("/api/reviews/{reviewId}")
    public ResponseEntity<DataResponse<SearchReviewResponse>> getOne(@PathVariable Long reviewId) {
        return ResponseEntity.ok(DataResponse.success(HttpStatus.OK.name(), reviewService.findOne(reviewId)));
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<MessageResponse<Void>> delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok(MessageResponse.success(HttpStatus.OK.name(), MSG_DELETE_REVIEW_ACCOUNT));
    }
}
