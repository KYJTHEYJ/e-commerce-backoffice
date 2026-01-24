package e3i2.ecommerce_backoffice.common.config;

import e3i2.ecommerce_backoffice.common.fillter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT 사용을 위해 Custom
 */

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // 시큐리티가 갖고 있는 passwordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    // 시큐리티 필터 체인
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF disable
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 disable
            .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 disable (UsernamePasswordAuthenticationFilter, DefaultLoginPageGeneratingFilter 비활성화)
            .httpBasic(AbstractHttpConfigurer::disable) // http basic 인증 disable
            .authorizeHttpRequests(auth -> auth // 인증 URL 체크
                    .requestMatchers("/api/auth/**").permitAll() // 인증 없이 접근 가능할 때 permitAll
                    .requestMatchers("/api/admins/signUp").permitAll()
                    .requestMatchers("/api/admins/login").permitAll()
                    .requestMatchers("/api/admins/{adminId:\\d+}/accept").hasRole("SUPER_ADMIN") // SUPER_ADMIN 만 접근 가능
                    .requestMatchers("/api/products").hasRole("SUPER_ADMIN") // SUPER_ADMIN 만 접근 가능
                    .anyRequest().authenticated()) // 나머지는 인증 필요
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가 **

        return http.build();
    }
}
