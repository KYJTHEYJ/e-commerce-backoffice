package e3i2.ecommerce_backoffice.domain.order.service;

import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import e3i2.ecommerce_backoffice.domain.order.dto.CreateOrderingRequest;
import e3i2.ecommerce_backoffice.domain.order.dto.CreateOrderingResponse;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingSeq;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingSeqRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class OrderingService {
    private final OrderingRepository orderRepository;
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

        Long nextOrderingSeq = orderingSeq.getNextOrderingSeq();

        return String.format("ORDER%s%03d", "-", nextOrderingSeq);
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

        if(product.getQuantity() < createOrderRequest.getOrderQuantity()) {
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

        Ordering saveOrder = orderRepository.save(ordering);
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

}
