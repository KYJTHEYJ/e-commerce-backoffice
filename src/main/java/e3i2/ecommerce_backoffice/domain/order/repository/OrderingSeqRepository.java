package e3i2.ecommerce_backoffice.domain.order.repository;

import e3i2.ecommerce_backoffice.domain.order.entity.OrderingSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderingSeqRepository extends JpaRepository<OrderingSeq, Long> {
    @Query("SELECT os FROM OrderingSeq os WHERE os.id = 'ORDER'")
    Optional<OrderingSeq> findById();
}
