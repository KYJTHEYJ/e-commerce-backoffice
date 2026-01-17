package e3i2.ecommerce_backoffice.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ordering_seq")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderingSeq {
    @Id
    private String id = "SEQ";

    @Column(nullable = false)
    private Long CurrentOrderSeq = 0L;

    public void update() {
        this.CurrentOrderSeq = this.CurrentOrderSeq + 1;
    }

    public Long getNextOrderingSeq() {
        update();
        return CurrentOrderSeq;
    }

    public static OrderingSeq register() {
        OrderingSeq orderingSeq = new OrderingSeq();
        orderingSeq.id = "SEQ";
        orderingSeq.CurrentOrderSeq = 0L;

        return new OrderingSeq();
    }

}
