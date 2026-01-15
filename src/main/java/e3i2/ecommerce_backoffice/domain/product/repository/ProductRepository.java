package e3i2.ecommerce_backoffice.domain.product.repository;

import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            SELECT p
            FROM Product p
            WHERE (LOWER(p.productName) LIKE CONCAT('%', LOWER(:productName), '%') OR :productName IS NULL)
            AND (p.category = :category OR :category IS NULL)
            AND (p.status = :status OR :status IS NULL)
            AND p.deleted = false
           """)
    Page<Product> findProducts(
            @Param("productName") String productName
            , @Param("category") ProductCategory category
            , @Param("status") ProductStatus status
            , Pageable pageable);

    Optional<Product> findByProductIdAndDeletedFalse(Long productId);
}
