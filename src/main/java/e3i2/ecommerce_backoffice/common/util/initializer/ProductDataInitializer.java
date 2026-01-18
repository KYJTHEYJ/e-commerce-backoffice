package e3i2.ecommerce_backoffice.common.util.initializer;

import e3i2.ecommerce_backoffice.domain.admin.entity.Admin;
import e3i2.ecommerce_backoffice.domain.admin.repository.AdminRepository;
import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.order.entity.OrderingSeq;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingSeqRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductCategory;
import e3i2.ecommerce_backoffice.domain.product.entity.ProductStatus;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component("productDataInit")
@Profile("local")
@RequiredArgsConstructor
@DependsOn("adminDataInit")
@Order(3)
public class ProductDataInitializer implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (productRepository.count() > 0) {
            return;
        }

        Admin admin = adminRepository.findAll().get(0);

        String[] productNameArr = {
                "테스트 제품1", "테스트 제품2", "테스트 제품3"
        };

        ProductCategory[] categoriesArr = {
                ProductCategory.ELECTRONICS
                ,ProductCategory.FOOD
                ,ProductCategory.CLOTH
        };

        Long[] priceArr = {
                1500000L, 12000L, 80000L
        };

        for (int i = 0; i < productNameArr.length; i++) {
            Product product = Product.register(
                    admin
                    , productNameArr[i]
                    , categoriesArr[i]
                    , priceArr[i]
                    , 5L
                    , ProductStatus.ON_SALE
            );

            productRepository.save(product);
        }
    }
}
