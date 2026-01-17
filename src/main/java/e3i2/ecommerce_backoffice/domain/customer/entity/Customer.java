package e3i2.ecommerce_backoffice.domain.customer.entity;

import e3i2.ecommerce_backoffice.common.entity.Base;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "customer_unique_email", columnNames = {"email"})
        , @UniqueConstraint(name = "customer_unique_phone", columnNames = {"phone"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String customerName;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @Column(nullable = false)
    private Long totalOrders;

    @Column(nullable = false)
    private BigInteger totalSpent;

    @Column(nullable = false)
    private Boolean deleted;
    private LocalDateTime deletedAt;

    public static Customer register(String customerName, String email, String phone, CustomerStatus customerStatus,  Long totalOrders, BigInteger totalSpent) {
        Customer customer = new Customer();
        customer.customerName = customerName;
        customer.email = email;
        customer.phone = phone;
        customer.customerStatus = customerStatus;
        customer.totalOrders = totalOrders;
        customer.totalSpent = totalSpent;
        customer.deleted = false;

        return customer;
    }

    public void update(String customerName, String email, String phone) {
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
    }

    public void statusChange(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
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
