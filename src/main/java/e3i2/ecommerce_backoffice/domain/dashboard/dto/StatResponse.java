package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({
        "summary"
        , "widgets"
        , "charts"
        , "recentOrders"
})
public class StatResponse {
    private final SummaryResponse summary;
    private final SearchWidgetsResponse widgets;
    private final ChartsResponse charts;
    private final List<SearchRecentListResponse> recentOrders;

    private StatResponse(
            SummaryResponse summary
            , SearchWidgetsResponse widgets
            , ChartsResponse charts
            , List<SearchRecentListResponse> recentOrders
    ) {
        this.summary = summary;
        this.widgets = widgets;
        this.charts = charts;
        this.recentOrders = recentOrders;
    }

    public static StatResponse register(
            SummaryResponse summary
            , SearchWidgetsResponse widgets
            , ChartsResponse charts
            , List<SearchRecentListResponse> recentOrders
    ) {
        return new StatResponse(
                summary
                , widgets
                , charts
                , recentOrders
        );
    }
}
