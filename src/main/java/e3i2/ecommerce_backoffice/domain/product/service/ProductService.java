package e3i2.ecommerce_backoffice.domain.product.service;

import e3i2.ecommerce_backoffice.domain.product.dto.*;
import e3i2.ecommerce_backoffice.domain.product.dto.common.ProductsWithPagination;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO admin 기능 구현시, 주석 단위 교체 필요
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public CreateProductResponse createProduct(@Valid CreateProductRequest request) {
        Product product = Product.register(
                request.getName()
                , request.getCategory()
                , request.getPrice()
                , request.getQuantity()
                , request.getStatus()
        );

        Product saveProduct = productRepository.save(product);

        return CreateProductResponse.register(
                saveProduct.getProductId()
                , saveProduct.getProductName()
                , saveProduct.getCategory().getCategoryCode()
                , saveProduct.getPrice()
                , saveProduct.getQuantity()
                , saveProduct.getStatus().getStatusCode()
                , saveProduct.getCreatedAt()
                //, saveProduct.getAdmin().getAdminId()
                //, saveProduct.getAdmin().getAdminName()
                //, saveProduct.getAdmin().getEmail()
        );
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ProductsWithPagination searchAllProduct(String productName, ProductCategory category, ProductStatus status, Integer page, Integer limit, String sortBy, String sortOrder) {
        List<SearchProductResponse> items = productRepository.findProducts(
                        productName
                        , category
                        , status,
                        PageRequest.of(page - 1, limit, Sort.by(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)))
                .stream()
                .map(product -> SearchProductResponse.register(
                        product.getProductId()
                        , product.getProductName()
                        , product.getCategory().getCategoryCode()
                        , product.getPrice()
                        , product.getQuantity()
                        , product.getStatus().getStatusCode()
                        , product.getCreatedAt()
                        //, saveProduct.getAdmin().getAdminId()
                        //, saveProduct.getAdmin().getAdminName()
                        //, saveProduct.getAdmin().getEmail()
                )).toList();

        return ProductsWithPagination.register(items, page, limit, (long) items.size());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SearchProductResponse searchProduct(Long productId) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));
        return SearchProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                //, saveProduct.getAdmin().getAdminId()
                //, saveProduct.getAdmin().getAdminName()
                //, saveProduct.getAdmin().getEmail()
        );
    }

    @Transactional
    public UpdateInfoProductResponse updateInfoProduct(Long productId, UpdateInfoProductRequest request) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));
        product.updateInfo(request.getProductName(), request.getCategory(), request.getPrice());
        return UpdateInfoProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                //, saveProduct.getAdmin().getAdminId()
                //, saveProduct.getAdmin().getAdminName()
                //, saveProduct.getAdmin().getEmail()
        );
    }

    @Transactional
    public UpdateQuantityProductResponse updateQuantityProduct(Long productId, UpdateQuantityProductRequest request) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));
        product.updateQunatity(request.getQuantity());
        return UpdateQuantityProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                //, saveProduct.getAdmin().getAdminId()
                //, saveProduct.getAdmin().getAdminName()
                //, saveProduct.getAdmin().getEmail()
        );
    }

    @Transactional
    public UpdateStatusProductResponse updateStatusProduct(Long productId, UpdateStatusProductRequest request) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));
        product.updateStatus(request.getStatus());
        return UpdateStatusProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                //, saveProduct.getAdmin().getAdminId()
                //, saveProduct.getAdmin().getAdminName()
                //, saveProduct.getAdmin().getEmail()
        );
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));
        product.delete();
    }
}
