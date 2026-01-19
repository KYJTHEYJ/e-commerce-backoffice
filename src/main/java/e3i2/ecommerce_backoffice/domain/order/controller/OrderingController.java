package e3i2.ecommerce_backoffice.domain.order.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.order.dto.*;
import e3i2.ecommerce_backoffice.domain.order.service.OrderingService;
import jakarta.validation.Valid;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static e3i2.ecommerce_backoffice.common.util.Constants.ADMIN_SESSION_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderingController {
    private final OrderingService orderingService;

    @PostMapping
    @LoginSessionCheck
    public ResponseEntity<DataResponse<CreateOrderingResponse>> createProduct(
            @Valid @RequestBody CreateOrderingRequest request,
            @SessionAttribute(ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), orderingService.createOrder(request, sessionAdmin)));
    }

    @GetMapping
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ItemsWithPagination<List<SearchOrderingResponse>>>> searchAllOrderResponse(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) OrderingStatus orderStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "orderAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder

    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(),
                orderingService.searchAllOrdering(orderNo, customerName, orderStatus, page, limit, sortBy, sortOrder)));
    }

    @GetMapping("/{orderId}")
    ResponseEntity<DataResponse<SearchOrderingResponse>> searchOrderResponse(
            @PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(),
                orderingService.searchOrdering(orderId)));
    }

    @PutMapping("/{orderId}/status")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ChangeOrderingStatusResponse>> changeOrderingStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody ChangeOrderingStatusRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), orderingService.updateStatusOrdering(orderId, request)));
    }

    @PutMapping("/{orderId}/cancel")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<CancelOrderingResponse>> cancelOrdering(
            @PathVariable Long orderId,
            @RequestBody CancelOrderingRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), orderingService.cancelOrdering(orderId, request)));
    }
}
