package e3i2.ecommerce_backoffice.domain.product.service;

import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import e3i2.ecommerce_backoffice.domain.product.dto.*;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import e3i2.ecommerce_backoffice.domain.product.repository.projection.ReviewSummary;
import e3i2.ecommerce_backoffice.domain.review.dto.SearchReviewResponse;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CreateProductResponse createProduct(@Valid CreateProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findById(sessionAdmin.getAdminId()).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));

        if(request.getCategory() == ProductCategory.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_PRODUCT_CATEGORY);
        }

        if(request.getStatus() == ProductStatus.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_PRODUCT_STATUS);
        }

        Product product = Product.register(
                admin
                , request.getProductName()
                , request.getCategory()
                , request.getPrice()
                , request.getQuantity()
                , request.getStatus()
        );

        Product saveProduct = productRepository.save(product);


        return CreateProductResponse.register(
                saveProduct.getProductId()
                , saveProduct.getProductName()
                , saveProduct.getCategory()
                , saveProduct.getPrice()
                , saveProduct.getQuantity()
                , saveProduct.getStatus()
                , saveProduct.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ItemsWithPagination<List<SearchProductResponse>> searchAllProduct(String productName, ProductCategory category, ProductStatus status, Integer page, Integer limit, String sortBy, String sortOrder) {
        Page<Product> products = productRepository.findProducts(productName, category, status, PageRequest.of(page - 1, limit, Sort.by(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)));

        List<SearchProductResponse> items = products.stream().map(product -> SearchProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus()
                , product.getCreatedAt()
                , product.getAdmin().getAdminId()
                , product.getAdmin().getAdminName()
                , product.getAdmin().getEmail()
        )).toList();

        return ItemsWithPagination.register(items, page, limit, products.getTotalElements());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SearchProductDetailResponse searchProduct(Long productId) {
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_PRODUCT));
        ReviewSummary reviewSummary = reviewRepository.findReviewSummaryByProductId(product);
        List<Review> recentReview = reviewRepository.findRecent3ReviewByProduct(product);

        SearchReviewSummaryResponse reviewSummaryResponse = SearchReviewSummaryResponse.register(
                reviewSummary.getAverageRating()
                , reviewSummary.getTotalReviews()
                , reviewSummary.getFiveStarCount()
                , reviewSummary.getFourStarCount()
                , reviewSummary.getThreeStarCount()
                , reviewSummary.getTwoStarCount()
                , reviewSummary.getOneStarCount()
        );

        List<SearchReviewResponse> reviewResponseList = recentReview
                .stream()
                .map(review ->
                    SearchReviewResponse.register(
                            review.getReviewId()
                            , review.getOrder().getOrderNo()
                            , product.getProductId()
                            , review.getCustomer().getCustomerId()
                            , review.getCustomer().getCustomerName()
                            , review.getCustomer().getEmail()
                            , product.getProductName()
                            , review.getRating()
                            , review.getContent()
                            , review.getCreatedAt()
                    )
                ).toList();

        return SearchProductDetailResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus()
                , product.getCreatedAt()
                , product.getAdmin().getAdminId()
                , product.getAdmin().getAdminName()
                , product.getAdmin().getEmail()
                , reviewSummaryResponse
                , reviewResponseList
        );
    }

    @Transactional
    public UpdateInfoProductResponse updateInfoProduct(Long productId, UpdateInfoProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(sessionAdmin.getAdminId()).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_PRODUCT));

        if(request.getCategory() == ProductCategory.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_PRODUCT_CATEGORY);
        }

        product.updateInfo(request.getProductName(), request.getCategory(), request.getPrice());

        return UpdateInfoProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus()
                , product.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional
    public UpdateQuantityProductResponse updateQuantityProduct(Long productId, UpdateQuantityProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(sessionAdmin.getAdminId()).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_PRODUCT));

        product.updateQuantity(request.getQuantity());

        return UpdateQuantityProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus()
                , product.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional
    public UpdateStatusProductResponse updateStatusProduct(Long productId, UpdateStatusProductRequest request, SessionAdmin sessionAdmin) {
        Admin admin = adminRepository.findByAdminIdAndDeletedFalse(sessionAdmin.getAdminId()).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_PRODUCT));

        if(request.getStatus() == ProductStatus.UNKNOWN) {
            throw new ServiceErrorException(ERR_NOT_FOUND_PRODUCT_STATUS);
        }

        product.updateStatus(request.getStatus());

        return UpdateStatusProductResponse.register(
                product.getProductId()
                , product.getProductName()
                , product.getCategory()
                , product.getPrice()
                , product.getQuantity()
                , product.getStatus()
                , product.getCreatedAt()
                , admin.getAdminId()
                , admin.getAdminName()
                , admin.getEmail()
        );
    }

    @Transactional
    public void deleteProduct(Long productId, SessionAdmin sessionAdmin) {
        adminRepository.findByAdminIdAndDeletedFalse(sessionAdmin.getAdminId()).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_ADMIN));
        Product product = productRepository.findByProductIdAndDeletedFalse(productId).orElseThrow(() -> new ServiceErrorException(ERR_NOT_FOUND_PRODUCT));

        product.delete();
    }
}
