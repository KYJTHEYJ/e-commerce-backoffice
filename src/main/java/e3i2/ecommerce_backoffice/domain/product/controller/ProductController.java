package e3i2.ecommerce_backoffice.domain.product.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // TODO 세션 관련 후처리 예정
    @PostMapping
    public ResponseEntity<ProductApiResponse<CreateProductResponse>> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductApiResponse.created(productService.createProduct(request)));
    }

    @GetMapping
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

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductApiResponse<SearchProductResponse>> serachProduct(@PathVariable Long product_id) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.searchProduct(product_id)));
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<ProductApiResponse<UpdateInfoProductResponse>> updateInfoProduct(
            @PathVariable Long product_id, @RequestBody UpdateInfoProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.updateInfoProduct(product_id, request)));
    }

    @PutMapping("/{product_id}/quantity")
    public ResponseEntity<ProductApiResponse<UpdateQuantityProductResponse>> updateQuantityProduct(
            @PathVariable Long product_id, @RequestBody UpdateQuantityProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.updateQuantityProduct(product_id, request)));
    }

    @PutMapping("/{product_id}/status")
    public ResponseEntity<ProductApiResponse<UpdateStatusProductResponse>> updateStatusProduct(
            @PathVariable Long product_id, @RequestBody UpdateStatusProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse.ok(productService.updateStatusProduct(product_id, request)));
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<ProductApiResponse2> deleteInfoProduct(@PathVariable Long product_id) {
        productService.deleteProduct(product_id);
        return ResponseEntity.status(HttpStatus.OK).body(ProductApiResponse2.deleted());
    }
}
