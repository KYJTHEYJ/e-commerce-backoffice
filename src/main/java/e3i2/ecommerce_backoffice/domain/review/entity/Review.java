package e3i2.ecommerce_backoffice.domain.review.entity;

import e3i2.ecommerce_backoffice.common.entity.Base;
import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private Integer rating;

    @Column(nullable=false)
    private Boolean deleted = false;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_id", value = ConstraintMode.NO_CONSTRAINT))
    private Ordering order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_id", value = ConstraintMode.NO_CONSTRAINT))
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_id", value = ConstraintMode.NO_CONSTRAINT))
    private Product product;

    public static Review register(
            String content, Integer rating, Ordering order, Customer customer, Product product
    ){
        Review review = new Review();
        review.content = content;
        review.rating = rating;
        review.order = order;
        review.customer = customer;
        review.product = product;

        return review;
    }

    public void delete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }

}
