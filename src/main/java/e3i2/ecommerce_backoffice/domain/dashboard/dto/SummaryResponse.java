package e3i2.ecommerce_backoffice.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "totalAdmins"
        , "activeAdmins"
        , "totalCustomers"
        , "activeCustomers"
        , "totalProduct"
        , "lowQuantityProducts"
        , "totalOrders"
        , "todayOrders"
        , "totalReviews"
        , "averageRating"
})
public class SummaryResponse {
    private final Long totalAdmins;
    private final Long activeAdmins;
    private final Long totalCustomers;
    private final Long activeCustomers;
    private final Long totalProducts;
    private final Long lowQuantityProducts;
    private final Long totalOrders;
    private final Long todayOrders;
    private final Long totalReviews;
    private final Double averageRating;

    private SummaryResponse(
            Long totalAdmins
            , Long activeAdmins
            , Long totalCustomers
            , Long activeCustomers
            , Long totalProducts
            , Long lowQuantityProducts
            , Long totalOrders
            , Long todayOrders
            , Long totalReviews
            , Double averageRating
    ) {
        this.totalAdmins = totalAdmins;
        this.activeAdmins = activeAdmins;
        this.totalCustomers = totalCustomers;
        this.activeCustomers = activeCustomers;
        this.totalProducts = totalProducts;
        this.lowQuantityProducts = lowQuantityProducts;
        this.totalOrders = totalOrders;
        this.todayOrders = todayOrders;
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
    }

    public static SummaryResponse register(
            Long totalAdmins
            , Long activeAdmins
            , Long totalCustomers
            , Long activeCustomers
            , Long totalProducts
            , Long lowQuantityProducts
            , Long totalOrders
            , Long todayOrders
            , Long totalReviews
            , Double averageRating
    ) {
        return new SummaryResponse(
                totalAdmins
                , activeAdmins
                , totalCustomers
                , activeCustomers
                , totalProducts
                , lowQuantityProducts
                , totalOrders
                , todayOrders
                , totalReviews
                , averageRating
        );
    }
}
