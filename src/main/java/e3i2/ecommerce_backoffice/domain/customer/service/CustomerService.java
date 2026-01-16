package e3i2.ecommerce_backoffice.domain.customer.service;

import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.customer.dto.*;
import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    // 고객 리스트 조회
    @Transactional(readOnly = true)
    public ItemsWithPagination<List<GetCustomerResponse>> findAll(String customerName, String email, Integer page, Integer limit, String sortBy, String sortOrder, CustomerStatus status) {
        Page<Customer> customers = customerRepository.findAllByFilters(customerName, email, status, PageRequest.of(page - 1, limit, Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));

        List<GetCustomerResponse> items = customers.stream().map(customer -> new GetCustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCustomerStatus().getStatusCode(),
                customer.getCreatedAt(),
                customer.getTotalOrders(),
                customer.getTotalSpent()
        )).toList();

        return ItemsWithPagination.register(items, page, limit, customers.getTotalElements());
    }

    // 고객 상세 조회
    @Transactional(readOnly = true)
    public GetCustomerResponse findOne(Long customerId) {
        Customer customer = customerRepository.findByCustomerIdAndDeletedFalse(customerId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_CUSTOMER)
        );
        return new GetCustomerResponse(customer.getCustomerId(), customer.getCustomerName(), customer.getEmail(), customer.getPhone(), customer.getCustomerStatus().getStatusCode(), customer.getCreatedAt(), customer.getTotalOrders(), customer.getTotalSpent());
    }

    // 고객 정보 수정
    @Transactional
    public PutInfoCustomerResponse updateInfo(Long customerId, @Valid PutInfoCustomerRequest request) {
        Customer customer = customerRepository.findByCustomerIdAndDeletedFalse(customerId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_CUSTOMER)
        );
        boolean emailExists = customerRepository.existsByEmail(request.getEmail());
        if (emailExists) {
            throw new ServiceErrorException(ErrorEnum.ERR_DUPLICATED_EMAIL);
        }
        boolean phoneExists = customerRepository.existsByPhone(request.getPhone());
        if (phoneExists) {
            throw new ServiceErrorException(ErrorEnum.ERR_DUPLICATED_PHONE);
        }
        customer.update(request.getCustomerName(), request.getEmail(), request.getPhone());
        return new PutInfoCustomerResponse(customer.getCustomerId(), customer.getCustomerName(), customer.getEmail(), customer.getPhone(), customer.getCustomerStatus().getStatusCode(), customer.getCreatedAt(), customer.getTotalOrders(), customer.getTotalSpent());

    }

    // 고객 상태 변경
    @Transactional
    public PutStatusCustomerResponse updateStatus(Long customerId, @Valid PutStatusCustomerRequest request) {
        Customer customer = customerRepository.findByCustomerIdAndDeletedFalse(customerId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_CUSTOMER)
        );
        customer.statusChange(request.getStatus());
        return new PutStatusCustomerResponse(customer.getCustomerId(), customer.getCustomerName(), customer.getEmail(), customer.getPhone(), customer.getCustomerStatus().getStatusCode(), customer.getCreatedAt(),  customer.getTotalOrders(), customer.getTotalSpent());
    }

    // 고객 삭제
    @Transactional
    public void delete(Long customerId) {
        Customer customer = customerRepository.findByCustomerIdAndDeletedFalse(customerId).orElseThrow(
                () -> new ServiceErrorException(ErrorEnum.ERR_NOT_FOUND_CUSTOMER)
        );
        if (customer.getTotalOrders() > 0) {
            throw new ServiceErrorException(ErrorEnum.ERR_DENY_CUSTOMER_ACCOUNT_DELETE);
        }
        customer.delete();
    }
}
