package e3i2.ecommerce_backoffice.domain.order.service;

import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.domain.order.dto.CancelOrderingRequest;
import e3i2.ecommerce_backoffice.domain.order.dto.CancelOrderingResponse;
import e3i2.ecommerce_backoffice.domain.order.dto.ChangeOrderingStatusRequest;
import e3i2.ecommerce_backoffice.domain.order.dto.ChangeOrderingStatusResponse;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class OrderingService {
    private final OrderingRepository orderingRepository;

    @Transactional
    public ChangeOrderingStatusResponse updateStatusOrdering(Long orderId, ChangeOrderingStatusRequest request, SessionAdmin sessionAdmin) {
        Ordering ordering = orderingRepository.findById(orderId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ORDER)
        );

        OrderingStatus current = ordering.getOrderStatus();
        OrderingStatus next = request.getStatus();


        //배송완료 상태는 변경 불가
        if (current == OrderingStatus.DELIVERED) {
            throw new ServiceErrorException(ERR_ORDER_CHANGE_FORBIDDEN);
        }

        //준비 중 -> 배송 중 -> 배송 완료 순으로만 상태 전이 가능
        if (!isValidNextStatus(current, next)) {
            throw new ServiceErrorException(ERR_ORDER_CHANGE_FORBIDDEN);
        }

        ordering.changeStatus(next);

        return ChangeOrderingStatusResponse.register(
                ordering.getOrderId(),
                ordering.getOrderNo(),
                ordering.getCustomer().getCustomerId(),
                ordering.getCustomer().getCustomerName(),
                ordering.getCustomer().getCustomerName(),
                ordering.getProduct().getProductId(),
                ordering.getProduct().getProductName(),
                ordering.getOrderQuantity(),
                ordering.getOrderTotalPrice(),
                ordering.getCreatedAt(),
                ordering.getOrderStatus()
        );
    }

    @Transactional
    public CancelOrderingResponse cancelOrdering(Long orderId, SessionAdmin sessionAdmin, CancelOrderingRequest request) {
        Ordering ordering = orderingRepository.findById(orderId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ORDER)
        );
        OrderingStatus current = ordering.getOrderStatus();
        if (current == OrderingStatus.DELIVERED) {
            throw new ServiceErrorException(ERR_ORDER_CHANGE_FORBIDDEN);
        }

        // 이미 취소된 주문
        if (current == OrderingStatus.CANCELLED) {
            throw new ServiceErrorException(ERR_ORDER_ALREADY_CANCELLED);
        }

        ordering.cancel(request.getCancelReason());

        //취소 수량만큼 재고 증가
        Product product = ordering.getProduct();
        product.restoreStock(ordering.getOrderQuantity());

        return CancelOrderingResponse.register(
                ordering.getOrderId(),
                ordering.getOrderNo(),
                ordering.getCustomer().getCustomerId(),
                ordering.getCustomer().getCustomerName(),
                ordering.getCustomer().getCustomerName(),
                ordering.getProduct().getProductId(),
                ordering.getProduct().getProductName(),
                ordering.getOrderQuantity(),
                ordering.getOrderTotalPrice(),
                ordering.getCreatedAt(),
                ordering.getOrderStatus(),
                ordering.getCancelReason()
        );
    }

    //상태 전이 검증 메서드(준비중 -> 배송중 -> 배송완료 순으로만 진행되도록)
    private boolean isValidNextStatus(OrderingStatus current, OrderingStatus next) {
        return switch (current) {
            case PREPARING -> next == OrderingStatus.SHIPPING;
            case SHIPPING -> next == OrderingStatus.DELIVERED;
            default -> false;
        };
    }
}
