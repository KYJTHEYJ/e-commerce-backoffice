package e3i2.ecommerce_backoffice.domain.review.repository;

import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewIdAndDeletedFalse(Long reviewId);

    @EntityGraph(attributePaths = {"customer", "product", "ordering"})
    @Query("""
        SELECT r
        FROM Review r
        JOIN r.customer c
        JOIN r.product p
        JOIN r.order o
        WHERE r.deleted = false
          AND (
              :keyword IS NULL
              OR LOWER(c.customerName) LIKE CONCAT('%', LOWER(:keyword), '%')
              OR LOWER(p.productName) LIKE CONCAT('%', LOWER(:keyword), '%')
          )
          AND (r.rating = :rating OR :rating IS NULL)
    """)
    Page<Review> findReviews(@Param("keyword") String keyword, @Param("rating") Integer rating, Pageable pageable);
}
