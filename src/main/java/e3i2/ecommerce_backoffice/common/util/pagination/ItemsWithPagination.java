package e3i2.ecommerce_backoffice.common.util.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemsWithPagination<T> {
    private T items;
    private Pagination pagination;

    public static <T> ItemsWithPagination<T> register(T items, Integer page, Integer limit, Long total) {
        ItemsWithPagination<T> ItemsWithPagination = new ItemsWithPagination<>();
        ItemsWithPagination.items = items;
        ItemsWithPagination.pagination = Pagination.register(
                page
                , limit
                , total
                , (int) Math.ceil((double) total / limit)
        );

        return ItemsWithPagination;
    }

}
