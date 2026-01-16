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
    private Long price;

    @Column(nullable = false)
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private Boolean deleted = false;
    private LocalDateTime deletedAt;

    public static Product register(Admin admin, String productName, ProductCategory category, Long price, Long quantity, ProductStatus status) {
        Product product = new Product();
        product.admin = admin;
        product.productName = productName;
        product.category = category;
        product.price = price;
        product.quantity = quantity;
        product.status = status;

        return product;
    }

    public void updateInfo(String productName, ProductCategory category, Long price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    public void updateQuantity(Long quantity) {
        this.quantity = quantity;

        if(!status.equals(ProductStatus.DISCONTINUE)) {
            if(quantity <= 0) {
                status = ProductStatus.SOLD_OUT;
            } else {
                status = ProductStatus.ON_SALE;
            }
        }
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    public void delete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }

    public void restore() {
        deleted = false;
        deletedAt = null;
    }

    //주문 취소 시 재고량 복구 메서드
    public void restoreStock(Long quantity) {
        this.quantity += quantity;

        if (this.status != ProductStatus.DISCONTINUE) {
            if (this.quantity > 0) {
                this.status = ProductStatus.ON_SALE;
            }
        }
    }
}
