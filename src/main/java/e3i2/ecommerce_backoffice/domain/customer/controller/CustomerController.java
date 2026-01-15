package e3i2.ecommerce_backoffice.domain.customer.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.customer.dto.CustomerResponse;
import e3i2.ecommerce_backoffice.domain.customer.dto.GetCustomerResponse;
import e3i2.ecommerce_backoffice.domain.customer.dto.PatchInfoCustomerRequest;
import e3i2.ecommerce_backoffice.domain.customer.dto.PatchStatusCustomerRequest;
import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import e3i2.ecommerce_backoffice.domain.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static e3i2.ecommerce_backoffice.common.util.Constants.ADMIN_SESSION_NAME;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    // 고객 리스트 조회
    @GetMapping("/api/admins/customers")
    @LoginSessionCheck
    public ResponseEntity<CustomerResponse<Page<GetCustomerResponse>>> getAll(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false)CustomerStatus status,
            @SessionAttribute(ADMIN_SESSION_NAME) SessionAdmin loginAdmin
            ) {
        return ResponseEntity.ok(customerService.findAll(customerName, email, page, size, sortBy, direction, status));
    }

    // 고객 상세 조회
    @GetMapping("/api/admins/customers/{customerId}")
    @LoginSessionCheck
    public ResponseEntity<CustomerResponse<GetCustomerResponse>> getOne(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.findOne(customerId));
    }

    // 고객 정보 수정
    @PatchMapping("/api/admins/customers/{customerId}/info")
    @LoginSessionCheck
    public ResponseEntity<Void> patchInfo(@PathVariable Long customerId, @Valid @RequestBody PatchInfoCustomerRequest request) {
        customerService.updateInfo(customerId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 고객 상태 변경
    @PatchMapping("/api/admins/customers/{customerId}/status")
    @LoginSessionCheck
    public ResponseEntity<Void> patchStatus(@PathVariable Long customerId, @Valid @RequestBody PatchStatusCustomerRequest request) {
        customerService.updateStatus(customerId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 고객 삭제
    @DeleteMapping("/api/admins/customers/{customerId}")
    @LoginSessionCheck
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        customerService.delete(customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
