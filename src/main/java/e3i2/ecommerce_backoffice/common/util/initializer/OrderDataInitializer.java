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

@Component("orderDataInit")
@Profile("local")
@RequiredArgsConstructor
@DependsOn({"adminDataInit", "customerDataInit", "productDataInit"})
@Order(4)
public class OrderDataInitializer implements ApplicationRunner {

    private final OrderingRepository orderingRepository;
    private final OrderingSeqRepository orderingSeqRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (orderingRepository.count() > 0) {
            return;
        }

        Admin admin = adminRepository.findAll().get(0);
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            return;
        }

        List<Product> products = productRepository.findAll();

        if (orderingSeqRepository.count() == 0) {
            OrderingSeq orderingSeq = OrderingSeq.register();
            orderingSeqRepository.save(orderingSeq);
        }

        for (int i = 0; i < 5; i++) {
            Customer customer = customers.get(new Random().nextInt(customers.size()));
            Product product = products.get(new Random().nextInt(products.size()));
            Long quantity = new Random().nextLong(1,5);

            OrderingSeq orderingSeq = orderingSeqRepository.findById().orElseGet(
                    () -> {
                        OrderingSeq newOrderingSeq = OrderingSeq.register();
                        return orderingSeqRepository.save(newOrderingSeq);
                    }
            );

            Ordering ordering = Ordering.register(
                    orderingSeq.getNextOrderNo(),
                    quantity,
                    product,
                    customer,
                    admin
            );

            Ordering saveOrder = orderingRepository.save(ordering);
            product.updateQuantity(product.getQuantity() - quantity);
            customer.updateOrderInfo(saveOrder.getOrderTotalPrice());
        }
    }
}
