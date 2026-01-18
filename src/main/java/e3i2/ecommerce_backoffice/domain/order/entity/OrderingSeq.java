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
    private String orderingSeqId = "ORDER";

    @Column(nullable = false)
    private Long currentOrderSeq = 0L;

    public void update() {
        this.currentOrderSeq = this.currentOrderSeq + 1;
    }

    public String getNextOrderNo() {
        update();
        return String.format("%s%s%03d", this.orderingSeqId, "-", this.currentOrderSeq);
    }

    public static OrderingSeq register() {
        OrderingSeq orderingSeq = new OrderingSeq();
        orderingSeq.orderingSeqId = "ORDER";
        orderingSeq.currentOrderSeq = 0L;

        return new OrderingSeq();
    }

}
