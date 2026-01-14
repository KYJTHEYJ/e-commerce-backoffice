package e3i2.ecommerce_backoffice.domain.product.dto.common;

import e3i2.ecommerce_backoffice.domain.product.dto.SearchProductResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductsWithPagination {
    private List<SearchProductResponse> items;
    private Pagination pagination;

    public static ProductsWithPagination regist(List<SearchProductResponse> items, Integer page, Integer limit, Long total) {
        ProductsWithPagination productsWithPagination = new ProductsWithPagination();
        productsWithPagination.items = items;
        productsWithPagination.pagination = Pagination.regist(
                page
                , limit
                , total
                , (int) Math.ceil((double) total / limit)
        );

        return productsWithPagination;
    }

}
