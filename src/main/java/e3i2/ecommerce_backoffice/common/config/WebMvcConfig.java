package e3i2.ecommerce_backoffice.common.config;

import e3i2.ecommerce_backoffice.common.interceptor.LoginSessionAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// TODO Stage 반영시 주석 삭제
// 스프링 MVC 설정을 커스터 마이즈 할 때 WebMvcConfig를 사용
// WebMvcConfigurer 와 같이 사용합니다
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginSessionAccessInterceptor loginSessionAccessInterceptor;

    // 인터셉터 등록시 사용
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginSessionAccessInterceptor);
    }
}
