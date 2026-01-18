package e3i2.ecommerce_backoffice.common.util.initializer;

import e3i2.ecommerce_backoffice.domain.customer.entity.Customer;
import e3i2.ecommerce_backoffice.domain.customer.entity.CustomerStatus;
import e3i2.ecommerce_backoffice.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Component("customerDataInit")
@Profile("local")
@RequiredArgsConstructor
@DependsOn("adminDataInit")
@Order(2)
public class CustomerDataInitializer implements ApplicationRunner {

    private final CustomerRepository customerRepository;

    // 고객 데이터 초기화
    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if(customerRepository.count() > 0) {
            return;
        }

        String[] customerName = {"강하윤", "송지호", "정유미", "홍예준", "문채원", "천수현", "강준규", "한서연", "최은우", "최원빈", "박서준", "이영희", "이서아", "채아윤", "최수진", "백하준", "유하린", "허지호", "오수아", "강태우"};
        String[] email = {"hayoon@example.com", "jiho@example.com", "yumi@example.com", "yejun@example.com", "chaewon@example.com", "soohyun@example.com", "junkyu@example.com", "seoyeon@example.com", "eunwoo@example.com", "wonbin@example.com", "seojoon@example.com", "younghee@example.com", "seoa@example.com", "ayoon@example.com", "sujin@example.com", "hajun@example.com", "harin@example.com", "jiho2@example.com", "sua@example.com", "taewoo@example.com"};
        IntStream.rangeClosed(1, 20).mapToObj(index -> Customer.register(
                String.format(customerName[index - 1])
                , String.format(email[index - 1])
                , String.format("010-%04d-%04d", index, index)
                , switch (index % 3) {
                    case 0 -> CustomerStatus.ACT;
                    case 1 -> CustomerStatus.IN_ACT;
                    case 2 -> CustomerStatus.SUSPEND;
                    default -> CustomerStatus.ACT;
                }
                ,0L
                ,0L
        )).forEach(customerRepository::save);
    }
}
