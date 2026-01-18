package e3i2.ecommerce_backoffice.domain.dashboard.service;

import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import e3i2.ecommerce_backoffice.domain.dashboard.dto.*;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderingRepository orderingRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public StatResponse getStat() {
        SummaryResponse summaryResponse = SummaryResponse.register(
                adminRepository.countByDeletedFalse()
                , adminRepository.countByAdminStatusDeletedFalse(AdminStatus.ACT)
                , customerRepository.countByDeletedFalse()
                , customerRepository.countByCustomerStatusDeletedFalse(CustomerStatus.ACT)
                , productRepository.countByDeletedFalse()
                , productRepository.countByProductQuantityLower5DeletedFalse()
                , orderingRepository.countByDeletedFalse()
                , orderingRepository.countByOrderQuantityLower5DeletedFalse()
                , reviewRepository.countByDeletedFalse()
                , reviewRepository.findAverageRating()
        );

        ChartsResponse chartsResponse = ChartsResponse.register(
                reviewRepository.countByRatingGroupByRating()
                        .stream()
                        .map(reviewRatingCount ->
                                ReviewRatingCountResponse.register(
                                        reviewRatingCount.getRating(),
                                        reviewRatingCount.getReviewCount()
                                )
                        ).toList()
                , customerRepository.countByStatusGroupByCustomerStatus()
                        .stream()
                        .map(customerStatusCount ->
                                CustomerStatusCountResponse.register(
                                        customerStatusCount.getCustomerStatus().getStatusCode(),
                                        customerStatusCount.getCustomerCount()
                                )
                        ).toList()
                , productRepository.countByCategoryGroupByCategory()
                        .stream()
                        .map(productCategoryCount ->
                                ProductCategoryCountResponse.register(
                                        productCategoryCount.getProductCategory().getCategoryCode(),
                                        productCategoryCount.getProductCount()
                                )
                        ).toList()
        );

        return StatResponse.register(summaryResponse, getWidgets(), chartsResponse, getRecentList());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SearchWidgetsResponse getWidgets() {
        // 오늘 날짜 범위
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 매출
        Long totalSales = orderingRepository.sumTotalSales();
        Long todaySales = orderingRepository.sumTodaySales(startOfDay, endOfDay);

        // 주문 상태별 수
        long preparingCount = orderingRepository.countByOrderStatusAndDeletedFalse(OrderingStatus.PREPARING);
        long shippingCount = orderingRepository.countByOrderStatusAndDeletedFalse(OrderingStatus.SHIPPING);
        long deliveredCount = orderingRepository.countByOrderStatusAndDeletedFalse(OrderingStatus.DELIVERED);

        // 재고
        long lowStockCount = productRepository.countByProductQuantityLower5DeletedFalse();
        long soldOutStockCount = productRepository.countByQuantityAndDeletedFalse(0L);

        return SearchWidgetsResponse.register(
                totalSales,
                todaySales,
                preparingCount,
                shippingCount,
                deliveredCount,
                lowStockCount,
                soldOutStockCount
        );
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SearchRecentListResponse> getRecentList() {

        List<Ordering> orders = orderingRepository.findRecentOrders(
                PageRequest.of(0, 10)
        );

        List<SearchRecentListResponse> responses = new ArrayList<>();

        for (Ordering order : orders) {
            Customer customer = order.getCustomer();
            Product product = order.getProduct();
            SearchRecentListResponse response = SearchRecentListResponse.register(
                    order.getOrderId(),
                    order.getOrderNo(),
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getEmail(),
                    product.getProductName(),
                    order.getOrderQuantity(),
                    order.getOrderTotalPrice(),
                    order.getCreatedAt(),
                    order.getOrderStatus()
            );

            responses.add(response);
        }

        return responses;
    }
}
