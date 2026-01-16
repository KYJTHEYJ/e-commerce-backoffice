package e3i2.ecommerce_backoffice.domain.review.repository;

import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepositorty extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewIdAndDeletedFalse(Long reviewId);
}
