package e3i2.ecommerce_backoffice.domain.product.dto.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pagination {
    private Integer page;
    private Integer limit;
    private Long total;
    private Integer totalPages;

    public static Pagination regist(Integer page, Integer limit, Long total, Integer totalPages) {
        Pagination pagination = new Pagination();
        pagination.page = page;
        pagination.limit = limit;
        pagination.total = total;
        pagination.totalPages = totalPages;

        return pagination;
    }
}
