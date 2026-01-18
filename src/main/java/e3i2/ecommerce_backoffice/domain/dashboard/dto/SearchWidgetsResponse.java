package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchWidgetsResponse {

    // 매출
    private Long totalSales;
    private Long todaySales;

    // 주문 상태
    private Long preparingCount;
    private Long shippingCount;
    private Long deliveredCount;

    // 재고
    private Long lowStockCount;
    private Long soldOutStockCount;

    public static SearchWidgetsResponse register(
            Long totalSales,
            Long todaySales,
            Long preparingCount,
            Long shippingCount,
            Long deliveredCount,
            Long lowStockCount,
            Long soldOutStockCount
    ) {
        SearchWidgetsResponse response = new SearchWidgetsResponse();
        response.totalSales = totalSales;
        response.todaySales = todaySales;
        response.preparingCount = preparingCount;
        response.shippingCount = shippingCount;
        response.deliveredCount = deliveredCount;
        response.lowStockCount = lowStockCount;
        response.soldOutStockCount = soldOutStockCount;
        return response;
    }
}
