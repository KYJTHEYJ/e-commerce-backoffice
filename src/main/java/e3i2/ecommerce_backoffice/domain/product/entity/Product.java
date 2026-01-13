package e3i2.ecommerce_backoffice.domain.product.entity;

import e3i2.ecommerce_backoffice.common.entity.Base;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false, foreignKey = @ForeignKey(name = "fk_admin_id", value = ConstraintMode.NO_CONSTRAINT))
    private Admin admin;

    @Column(nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private Boolean deleted = false;
    private LocalDateTime deletedAt;

    public static Product regist(Admin admin, String productName, ProductCategory category, Integer price, Integer quantity, ProductStatus status) {
        Product product = new Product();
        product.admin = admin;
        product.productName = productName;
        product.category = category;
        product.price = price;
        product.quantity = quantity;
        product.status = status;

        return product;
    }

    public void delete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }

    public void restore() {
        deleted = false;
        deletedAt = null;
    }
}
