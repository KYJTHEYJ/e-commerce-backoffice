package e3i2.ecommerce_backoffice.domain.product.repository;

import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
