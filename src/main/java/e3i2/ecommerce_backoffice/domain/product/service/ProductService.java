package e3i2.ecommerce_backoffice.domain.product.service;

import e3i2.ecommerce_backoffice.domain.admin.dto.common.SessionAdmin;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
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
    private final AdminRepository adminRepository;

    @Transactional
    public CreateProductResponse createProduct(@Valid CreateProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findById(sessionAdmin.getAdminId()).orElseThrow(() -> new IllegalStateException("로그인 계정의 관리자 정보를 찾을 수 없습니다"));

        Product product = Product.regist(
                admin
                , request.getName()
                , request.getCategory()
                , request.getPrice()
                , request.getQuantity()
                , request.getStatus()
        );

        Product saveProduct = productRepository.save(product);

        return CreateProductResponse.regist(
                saveProduct.getProductId()
                , saveProduct.getProductName()
                , saveProduct.getCategory().getCategoryCode()
                , saveProduct.getPrice()
                , saveProduct.getQuantity()
                , saveProduct.getStatus().getStatusCode()
                , saveProduct.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
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
                .map(product -> SearchProductResponse.regist(
                        product.getProductId()
                        , product.getProductName()
                        , product.getCategory().getCategoryCode()
                        , product.getPrice()
                        , product.getQuantity()
                        , product.getStatus().getStatusCode()
                        , product.getCreatedAt()
                        , product.getAdmin().getAdminId()
                        , product.getAdmin().getAdminName()
                        , product.getAdmin().getEmail()
                )).toList();

        return ProductsWithPagination.regist(items, page, limit, (long) items.size());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SearchProductResponse searchProduct(Long productId) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));
        return SearchProductResponse.regist(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                , product.getAdmin().getAdminId()
                , product.getAdmin().getAdminName()
                , product.getAdmin().getEmail()
        );
    }

    @Transactional
    public UpdateInfoProductResponse updateInfoProduct(Long productId, UpdateInfoProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findById(sessionAdmin.getAdminId()).orElseThrow(() -> new IllegalStateException("로그인 계정의 관리자 정보를 찾을 수 없습니다"));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));

        product.updateInfo(request.getProductName(), request.getCategory(), request.getPrice());

        return UpdateInfoProductResponse.regist(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional
    public UpdateQuantityProductResponse updateQuantityProduct(Long productId, UpdateQuantityProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findById(sessionAdmin.getAdminId()).orElseThrow(() -> new IllegalStateException("로그인 계정의 관리자 정보를 찾을 수 없습니다"));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));

        product.updateQuantity(request.getQuantity());

        return UpdateQuantityProductResponse.regist(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional
    public UpdateStatusProductResponse updateStatusProduct(Long productId, UpdateStatusProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findById(sessionAdmin.getAdminId()).orElseThrow(() -> new IllegalStateException("로그인 계정의 관리자 정보를 찾을 수 없습니다"));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));

        product.updateStatus(request.getStatus());

        return UpdateStatusProductResponse.regist(
                product.getProductId()
                , product.getProductName()
                , product.getCategory().getCategoryCode()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus().getStatusCode()
                , product.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional
    public void deleteProduct(Long productId, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findById(sessionAdmin.getAdminId()).orElseThrow(() -> new IllegalStateException("로그인 계정의 관리자 정보를 찾을 수 없습니다"));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다"));

        product.delete();
    }
}
