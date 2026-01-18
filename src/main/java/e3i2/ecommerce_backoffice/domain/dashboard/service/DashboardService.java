package e3i2.ecommerce_backoffice.domain.dashboard.service;

import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import e3i2.ecommerce_backoffice.domain.dashboard.dto.SearchRecentListResponse;
import e3i2.ecommerce_backoffice.domain.dashboard.dto.SearchWidgetsResponse;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingSeqRepository;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderingRepository orderingRepository;
    private final ProductRepository productRepository;
    private final OrderingSeqRepository orderingSeqRepository;
    private final CustomerRepository customerRepository;

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
        long lowStockCount = productRepository.countLowStockProducts();
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

    public List<SearchRecentListResponse> getRecentList() {
        return null;
    }
}
