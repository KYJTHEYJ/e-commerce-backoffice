package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
    "totalSales",
    "todaySales",
    "preparingCount",
    "shippingCount",
    "deliveredCount",
    "lowStockCount",
    "soldOutStockCount"
})
public class SearchWidgetsResponse {
    private final Long totalSales;
    private final Long todaySales;

    private final Long preparingCount;
    private final Long shippingCount;
    private final Long deliveredCount;

    private final Long lowStockCount;
    private final Long soldOutStockCount;

    private SearchWidgetsResponse(
            Long totalSales,
            Long todaySales,
            Long preparingCount,
            Long shippingCount,
            Long deliveredCount,
            Long lowStockCount,
            Long soldOutStockCount
    ) {
        this.totalSales = totalSales;
        this.todaySales = todaySales;
        this.preparingCount = preparingCount;
        this.shippingCount = shippingCount;
        this.deliveredCount = deliveredCount;
        this.lowStockCount = lowStockCount;
        this.soldOutStockCount = soldOutStockCount;
    }

    public static SearchWidgetsResponse register(
            Long totalSales,
            Long todaySales,
            Long preparingCount,
            Long shippingCount,
            Long deliveredCount,
            Long lowStockCount,
            Long soldOutStockCount
    ) {
        return new SearchWidgetsResponse(totalSales, todaySales, preparingCount, shippingCount, deliveredCount, lowStockCount, soldOutStockCount);
    }
}
