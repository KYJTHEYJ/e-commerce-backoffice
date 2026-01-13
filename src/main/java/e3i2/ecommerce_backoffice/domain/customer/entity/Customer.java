package e3i2.ecommerce_backoffice.domain.customer.entity;

import e3i2.ecommerce_backoffice.common.entity.Base;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "customer_unique_email"
                , columnNames = {"email"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @Column(nullable = false)
    private Boolean deleted;
    private LocalDateTime deletedAt;

    public static Customer regist(String email, String customerName, String phone, CustomerStatus customerStatus) {
        Customer customer = new Customer();
        customer.email = email;
        customer.customerName = customerName;
        customer.phone = phone;
        customer.customerStatus = customerStatus;
        customer.deleted = false;

        return customer;
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
