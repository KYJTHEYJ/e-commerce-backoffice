package e3i2.ecommerce_backoffice.common.util.initializer;

import e3i2.ecommerce_backoffice.domain.review.repository.ReviewRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Component
@Profile("local")
@RequiredArgsConstructor
public class ReviewDataInitializer implements ApplicationRunner {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if(reviewRepository.count() > 0) {
            return;
        }
        // 리뷰 데이터 초기화 로직 필요

    }
}
