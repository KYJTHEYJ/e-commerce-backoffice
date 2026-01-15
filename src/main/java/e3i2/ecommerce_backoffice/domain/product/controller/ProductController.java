package e3i2.ecommerce_backoffice.domain.product.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.product.dto.*;
import e3i2.ecommerce_backoffice.domain.product.dto.common.ProductApiResponse;
import e3i2.ecommerce_backoffice.domain.product.dto.common.ProductApiResponse2;
import e3i2.ecommerce_backoffice.domain.product.dto.common.ProductsWithPagination;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static e3i2.ecommerce_backoffice.common.util.Constants.ADMIN_SESSION_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse<CreateProductResponse>> createProduct(
            @RequestBody CreateProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductApiResponse.created(productService.createProduct(request, sessionAdmin)));
    }

    @GetMapping
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse<ProductsWithPagination>> searchProducts(
            @RequestParam(required = false) String productName
            , @RequestParam(defaultValue = "1") Integer page
            , @RequestParam(defaultValue = "10") Integer limit
            , @RequestParam(defaultValue = "createdAt") String sortBy
            , @RequestParam(defaultValue = "desc") String sortOrder
            , @RequestParam(required = false) ProductCategory category
            , @RequestParam(required = false) ProductStatus status
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.searchAllProduct(productName, category, status, page, limit, sortBy, sortOrder)));
    }

    @GetMapping("/{productId}")
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse<SearchProductResponse>> searchProduct(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.searchProduct(productId)));
    }

    @PutMapping("/{productId}")
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse<UpdateInfoProductResponse>> updateInfoProduct(
            @PathVariable Long productId
            , @RequestBody UpdateInfoProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.updateInfoProduct(productId, request, sessionAdmin)));
    }

    @PutMapping("/{productId}/quantity")
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse<UpdateQuantityProductResponse>> updateQuantityProduct(
            @PathVariable Long productId
            , @RequestBody UpdateQuantityProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.updateQuantityProduct(productId, request, sessionAdmin)));
    }

    @PutMapping("/{productId}/status")
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse<UpdateStatusProductResponse>> updateStatusProduct(
            @PathVariable Long productId
            , @RequestBody UpdateStatusProductRequest request
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.updateStatusProduct(productId, request, sessionAdmin)));
    }

    @DeleteMapping("/{productId}")
    @LoginSessionCheck
    public ResponseEntity<ProductApiResponse2> deleteInfoProduct(
            @PathVariable Long productId
            , @SessionAttribute(name = ADMIN_SESSION_NAME) SessionAdmin sessionAdmin
    ) {
        productService.deleteProduct(productId, sessionAdmin);
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse2.deleted());
    }
}
