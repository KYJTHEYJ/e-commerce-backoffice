package e3i2.ecommerce_backoffice.domain.product.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.product.dto.*;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static e3i2.ecommerce_backoffice.common.util.Constants.ADMIN_SESSION_NAME;
import static e3i2.ecommerce_backoffice.common.util.Constants.MSG_DELETE_PRODUCT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @LoginSessionCheck
    public ResponseEntity<DataResponse<CreateProductResponse>> createProduct(
            @RequestBody CreateProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.success(HttpStatus.CREATED.name(), productService.createProduct(request, sessionAdmin)));
    }

    @GetMapping
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ItemsWithPagination<List<SearchProductResponse>>>> searchProducts(
            @RequestParam(required = false) String productName
            , @RequestParam(defaultValue = "1") Integer page
            , @RequestParam(defaultValue = "10") Integer limit
            , @RequestParam(defaultValue = "createdAt") String sortBy
            , @RequestParam(defaultValue = "desc") String sortOrder
            , @RequestParam(required = false) ProductCategory category
            , @RequestParam(required = false) ProductStatus status
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), productService.searchAllProduct(productName, category, status, page, limit, sortBy, sortOrder)));
    }

    @GetMapping("/{productId}")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<SearchProductDetailResponse>> searchProduct(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), productService.searchProduct(productId)));
    }

    @PutMapping("/{productId}")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<UpdateInfoProductResponse>> updateInfoProduct(
            @PathVariable Long productId
            , @RequestBody UpdateInfoProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), productService.updateInfoProduct(productId, request, sessionAdmin)));
    }

    @PutMapping("/{productId}/quantity")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<UpdateQuantityProductResponse>> updateQuantityProduct(
            @PathVariable Long productId
            , @RequestBody UpdateQuantityProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), productService.updateQuantityProduct(productId, request, sessionAdmin)));
    }

    @PutMapping("/{productId}/status")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<UpdateStatusProductResponse>> updateStatusProduct(
            @PathVariable Long productId
            , @RequestBody UpdateStatusProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), productService.updateStatusProduct(productId, request, sessionAdmin)));
    }

    @DeleteMapping("/{productId}")
    @LoginSessionCheck
    public ResponseEntity<MessageResponse<Void>> deleteInfoProduct(
            @PathVariable Long productId
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        productService.deleteProduct(productId, sessionAdmin);
        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.success(HttpStatus.OK.name(), MSG_DELETE_PRODUCT));
    }
}
