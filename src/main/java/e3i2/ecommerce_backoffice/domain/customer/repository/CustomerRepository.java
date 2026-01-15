package e3i2.ecommerce_backoffice.domain.customer.repository;

import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE " +
            "(:customerName IS NULL OR c.customerName LIKE %:customerName) AND " +
            "(:email IS NULL OR c.email LIKE %:email) AND " +
            "(:status IS NULL OR c.customerStatus = :status) AND " +
            "c.deleted = false")
    Page<Customer> findAllByFilters(@Param("customerName") String customerName, @Param("email") String email ,@Param("status")CustomerStatus status, Pageable pageable);

    Optional<Customer> findByCustomerIdAndDeletedFalse(Long customerId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
