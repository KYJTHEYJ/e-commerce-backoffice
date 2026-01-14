package e3i2.ecommerce_backoffice.domain.product.service;

import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.product.dto.CreateProductRequest;
import e3i2.ecommerce_backoffice.domain.product.dto.CreateProductResponse;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public CreateProductResponse createProduct(@Valid CreateProductRequest request) {
        Product product = Product.regist(
                request.getName()
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
                        //, saveProduct.getAdmin().getAdminId()
                        //, saveProduct.getAdmin().getAdminName()
                        //, saveProduct.getAdmin().getEmail()
        );
    }
}
