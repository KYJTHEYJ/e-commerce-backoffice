package e3i2.ecommerce_backoffice.domain.review.repository;

import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.repository.projection.ReviewRatingCount;
import e3i2.ecommerce_backoffice.domain.product.repository.projection.ReviewSummary;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewIdAndDeletedFalse(Long reviewId);

    @EntityGraph(attributePaths = {"customer", "product", "order"})
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

    @Query("""
            SELECT ifnull(round(avg(r.rating), 1), 0) AS averageRating
            , count(r.reviewId) AS totalReviews
            , (SELECT count(r.reviewId) FROM Review r WHERE r.rating = 5 and r.product = :product) AS fiveStarCount
            , (SELECT count(r.reviewId) FROM Review r WHERE r.rating = 4 and r.product = :product) AS fourStarCount
            , (SELECT count(r.reviewId) FROM Review r WHERE r.rating = 3 and r.product = :product) AS threeStarCount
            , (SELECT count(r.reviewId) FROM Review r WHERE r.rating = 2 and r.product = :product) AS twoStarCount
            , (SELECT count(r.reviewId) FROM Review r WHERE r.rating = 1 and r.product = :product) AS oneStarCount
            FROM Review r
            INNER JOIN r.product
            WHERE r.product = :product
              AND r.deleted = false
           """)
    ReviewSummary findReviewSummaryByProductId(@Param("product") Product product);

    @Query("SELECT r FROM Review r WHERE r.product = :product ORDER BY r.createdAt DESC LIMIT 3")
    List<Review> findRecent3ReviewByProduct(Product product);

    Long countByDeletedFalse();

    @Query("""
           SELECT ifnull(round(avg(r.rating), 1), 0) AS averageRating
           FROM Review r
           WHERE r.deleted = false
           """)
    Double findAverageRating();

    @Query("""
        SELECT r.rating AS rating, COUNT(r.reviewId) AS reviewCount
        FROM Review r
        WHERE r.deleted = false
        GROUP BY r.rating
        ORDER BY r.rating
        """)
    List<ReviewRatingCount> countByRatingGroupByRating();
}
