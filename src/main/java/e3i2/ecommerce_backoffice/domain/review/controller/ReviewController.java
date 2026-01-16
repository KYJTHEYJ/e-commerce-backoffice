package e3i2.ecommerce_backoffice.domain.review.controller;

import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.domain.review.dto.GetReviewResponse;
import e3i2.ecommerce_backoffice.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_DELETE_REVIEW_ACCOUNT;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/api/admins/reviews/{reviewId}")
    public ResponseEntity<DataResponse<GetReviewResponse>> getOne(@PathVariable Long reviewId) {
        return ResponseEntity.ok(DataResponse.success(HttpStatus.OK.name(), reviewService.findOne(reviewId)));
    }

    @DeleteMapping("/api/admins/reviews/{reviewId}")
    public ResponseEntity<MessageResponse<Void>> delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok(MessageResponse.success(HttpStatus.OK.name(), MSG_DELETE_REVIEW_ACCOUNT));
    }
}
