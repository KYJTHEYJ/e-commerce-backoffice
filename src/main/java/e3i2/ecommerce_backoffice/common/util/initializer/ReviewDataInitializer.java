package e3i2.ecommerce_backoffice.common.util.initializer;

import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import e3i2.ecommerce_backoffice.domain.order.entity.Ordering;
import e3i2.ecommerce_backoffice.domain.order.repository.OrderingRepository;
import e3i2.ecommerce_backoffice.domain.product.entity.Product;
import e3i2.ecommerce_backoffice.domain.product.repository.ProductRepository;
import e3i2.ecommerce_backoffice.domain.review.entity.Review;
import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component("reviewDataInit")
@Profile("local")
@RequiredArgsConstructor
@DependsOn("orderDataInit")
@Order(5)
public class ReviewDataInitializer implements ApplicationRunner {

    private final ReviewRepository reviewRepository;
    private final OrderingRepository orderingRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (reviewRepository.count() > 0) {
            return;
        }

        List<Ordering> orderings = orderingRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (orderings.isEmpty() || customers.isEmpty() || products.isEmpty()) {
            log.info("리뷰 데이터 생성 실패");
            return;
        }

        for (int i = 0; i < 5; i++) {
            Ordering ordering = orderings.get(i);
            Customer customer = customers.get(new Random().nextInt(customers.size()));
            Product product = products.get(new Random().nextInt(products.size()));
            String content = "테스트 리뷰";
            Integer rating = new Random().nextInt(1, 5);

            Review review = Review.register(
                content,
                rating,
                ordering,
                customer,
                product
            );

            reviewRepository.save(review);
        }
    }
}
