package e3i2.ecommerce_backoffice.domain.product.controller;

import e3i2.ecommerce_backoffice.domain.product.dto.CreateProductRequest;
import e3i2.ecommerce_backoffice.domain.product.dto.CreateProductResponse;
import e3i2.ecommerce_backoffice.domain.product.dto.common.ProductApiResponse;
import e3i2.ecommerce_backoffice.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // TODO 세션 관련 후처리 예정
    @PostMapping("/api/products")
    public ResponseEntity<ProductApiResponse<CreateProductResponse>> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductApiResponse.created(productService.createProduct(request)));
    }
}
