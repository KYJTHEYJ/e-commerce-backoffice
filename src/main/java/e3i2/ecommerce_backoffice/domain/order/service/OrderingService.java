package e3i2.ecommerce_backoffice.domain.order.service;

import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.order.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingSeq;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingSeqRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;

import java.util.List;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_NOT_FOUND_ORDER;

@Service
@RequiredArgsConstructor
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final OrderingSeqRepository orderingSeqRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    private String getOrderingSeq() {
        OrderingSeq orderingSeq = orderingSeqRepository.findById().orElseGet(
                () -> {
                    OrderingSeq newOrderingSeq = OrderingSeq.register();
                    return orderingSeqRepository.save(newOrderingSeq);
                }
        );

        return orderingSeq.getNextOrderNo();
    }

    @Transactional
    public CreateOrderingResponse createOrder(CreateOrderingRequest createOrderRequest, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(sessionAdmin.getAdminId()).orElseThrow(() -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_ADMIN));
        Product product = productRepository.findByProductIdAndDeletedFalse(createOrderRequest.getProductId()).orElseThrow(() -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_PRODUCT));
        Customer customer = customerRepository.findByCustomerIdAndDeletedFalse(createOrderRequest.getCustomerId()).orElseThrow(() -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_CUSTOMER));

        if (product.getStatus().equals(ProductStatus.DISCONTINUE)) {
            throw new ServiceErrorException(ERR_ORDER_TO_DISCONTINUE);
        }

        if (product.getStatus().equals(ProductStatus.SOLD_OUT)) {
            throw new ServiceErrorException(ERR_ORDER_TO_SOLD_OUT);
        }

        if (product.getQuantity() < createOrderRequest.getOrderQuantity()) {
            throw new ServiceErrorException(ERR_ORDER_TO_QUANTITY_OVER);
        }

        String orderingSeq = getOrderingSeq();

        Ordering ordering = Ordering.register(
                orderingSeq
                , createOrderRequest.getOrderQuantity()
                , product
                , customer
                , admin
        );

        Ordering saveOrder = orderingRepository.save(ordering);
        product.updateQuantity(product.getQuantity() - createOrderRequest.getOrderQuantity());

        return CreateOrderingResponse.register(
                saveOrder.getOrderId()
                , saveOrder.getOrderNo()
                , saveOrder.getOrderAt()
                , saveOrder.getOrderStatus().getStatusCode()
                , saveOrder.getCustomer().getCustomerId()
                , saveOrder.getCustomer().getCustomerName()
                , saveOrder.getProduct().getProductId()
                , saveOrder.getProduct().getProductName()
                , saveOrder.getProduct().getPrice()
                , saveOrder.getOrderQuantity()
                , saveOrder.getOrderTotalPrice()
                , saveOrder.getAdmin().getAdminId()
                , saveOrder.getAdmin().getAdminName()
                , saveOrder.getAdmin().getEmail()
        );
    }

    // 주문 리스트 통합 조회
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ItemsWithPagination<List<SearchOrderingResponse>> searchAllOrdering(
            String orderNo, String customerName, OrderingStatus orderStatus, Integer page, Integer limit, String sortBy, String sortOrder) {
        Page<Ordering> orders = orderingRepository.findOrders(
                orderNo,
                customerName,
                orderStatus,
                PageRequest.of(page - 1,
                        limit,
                        Sort.by(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
                )
        );

        List<SearchOrderingResponse> items = orders.stream().map(order -> SearchOrderingResponse.register(
                order.getOrderId(),
                order.getOrderNo(),
                order.getCustomer().getCustomerId(),
                order.getCustomer().getCustomerName(),
                order.getProduct().getProductId(),
                order.getProduct().getProductName(),
                order.getOrderQuantity(),
                order.getOrderTotalPrice(),
                order.getOrderAt(),
                order.getOrderStatus(),
                order.getAdmin().getAdminId(),
                order.getAdmin().getAdminName(),
                order.getAdmin().getRole()
        )).toList();

        return ItemsWithPagination.register(items, page, limit, orders.getTotalElements());
    }

    // 주문 상세 조회
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SearchOrderingResponse searchOrdering(Long orderId) {
        Ordering order = orderingRepository.findByOrderIdAndDeletedFalse(orderId).
                orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ORDER));
        return SearchOrderingResponse.register(
                order.getOrderId(),
                order.getOrderNo(),
                order.getCustomer().getCustomerId(),
                order.getCustomer().getCustomerName(),
                order.getProduct().getProductId(),
                order.getProduct().getProductName(),
                order.getOrderQuantity(),
                order.getOrderTotalPrice(),
                order.getOrderAt(),
                order.getOrderStatus(),
                order.getAdmin().getAdminId(),
                order.getAdmin().getAdminName(),
                order.getAdmin().getRole()
        );
    }

    @Transactional
    public ChangeOrderingStatusResponse updateStatusOrdering(Long orderId, ChangeOrderingStatusRequest request) {
        Ordering ordering = orderingRepository.findById(orderId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ORDER)
        );

        OrderingStatus current = ordering.getOrderStatus();
        OrderingStatus next = request.getStatus();


        //배송완료 상태는 변경 불가
        if (current == OrderingStatus.DELIVERED) {
            throw new ServiceErrorException(ERR_ORDER_PROCESSING_DELIVERED_FORBIDDEN);
        }

        //준비 중 -> 배송 중 -> 배송 완료 순으로만 상태 전이 가능
        if (!isValidNextStatus(current, next)) {
            throw new ServiceErrorException(ERR_ORDER_STATUS_INVALID_TRANSITION);
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
    public CancelOrderingResponse cancelOrdering(Long orderId, CancelOrderingRequest request) {
        Ordering ordering = orderingRepository.findById(orderId).orElseThrow(
                () -> new ServiceErrorException(ERR_NOT_FOUND_ORDER)
        );

        OrderingStatus current = ordering.getOrderStatus();
        if (current == OrderingStatus.DELIVERED) {
            throw new ServiceErrorException(ERR_ORDER_PROCESSING_DELIVERED_FORBIDDEN);
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

    // 상태 전이 검증 메서드 (준비중 → 배송중 → 배송완료)
    private boolean isValidNextStatus(OrderingStatus current, OrderingStatus next) {
        if (current == OrderingStatus.PREPARING) {
            return next == OrderingStatus.SHIPPING;
        }

        if (current == OrderingStatus.SHIPPING) {
            return next == OrderingStatus.DELIVERED;
        }

        return false;
    }
}
