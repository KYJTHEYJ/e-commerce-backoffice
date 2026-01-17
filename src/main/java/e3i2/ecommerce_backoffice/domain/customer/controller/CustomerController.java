package e3i2.ecommerce_backoffice.domain.customer.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.customer.dto.*;
import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import e3i2.ecommerce_backoffice.domain.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_DELETE_CUSTOMER_ACCOUNT;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    // 고객 리스트 조회
    @GetMapping("/api/customers")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ItemsWithPagination<List<GetCustomerResponse>>>> getAll(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) CustomerStatus status
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), customerService.findAll(customerName, email, page, limit, sortBy, sortOrder, status)));
    }

    // 고객 상세 조회
    @GetMapping("/api/customers/{customerId}")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<GetCustomerResponse>> getOne(@PathVariable Long customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), customerService.findOne(customerId)));
    }

    // 고객 정보 수정
    @PutMapping("/api/customers/{customerId}/info")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<PutInfoCustomerResponse>> putInfo(@PathVariable Long customerId, @Valid @RequestBody PutInfoCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), customerService.updateInfo(customerId, request)));
    }

    // 고객 상태 변경
    @PutMapping("/api/customers/{customerId}/status")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<PutStatusCustomerResponse>> putStatus(@PathVariable Long customerId, @Valid @RequestBody PutStatusCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), customerService.updateStatus(customerId, request)));
    }

    // 고객 삭제
    @DeleteMapping("/api/customers/{customerId}")
    @LoginSessionCheck
    public ResponseEntity<MessageResponse<Void>> delete(@PathVariable Long customerId) {
        customerService.delete(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.success(HttpStatus.OK.name(), MSG_DELETE_CUSTOMER_ACCOUNT));
    }
}
