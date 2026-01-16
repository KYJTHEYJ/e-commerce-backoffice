package e3i2.ecommerce_backoffice.domain.review.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.review.dto.SearchReviewListResponse;
import e3i2.ecommerce_backoffice.domain.review.service.ReviewService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 리스트 조회
    @GetMapping("/api/admins/reviews")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ItemsWithPagination<List<SearchReviewListResponse>>>> getReviewList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) @Min(1) @Max(5) Integer rating
    ) {
        ItemsWithPagination<List<SearchReviewListResponse>> response = reviewService.getReviewList(
                keyword, page, size, sortBy, sortOrder, rating
        );

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }
}
