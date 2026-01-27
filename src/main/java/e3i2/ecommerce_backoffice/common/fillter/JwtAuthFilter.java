package e3i2.ecommerce_backoffice.common.fillter;

import e3i2.ecommerce_backoffice.common.auth.service.AdminDetailService;
import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.common.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Order(3)
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AdminDetailService adminDetailService;

    // 인증이 필요 없는 URL 패턴
    private static final List<String> PERMIT_ALL_PATH_ANT_STYLE = List.of(
            "/api/auth/**",
            "/api/admins/signUp",
            "/api/admins/login"
    );

    // URL 패턴 검증용 (Ant 스타일)
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청
        log.info("JwtAuthFilter IN");

        // 올바른 토큰이 있는지?
        // Authorization 토큰
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            MessageResponse errorResponse = MessageResponse.fail(HttpStatus.UNAUTHORIZED.name(),  "인증 정보가 없습니다");
            String json = objectMapper.writeValueAsString(errorResponse);

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(json);
            return;
        }

        // Bearer 검증 잘라내기
        String token = authorization.substring("Bearer ".length());

        if(!jwtUtil.validateToken(token)) {
            MessageResponse errorResponse = MessageResponse.fail(HttpStatus.UNAUTHORIZED.name(),  "인증 정보에 대한 확인이 필요합니다");
            String json = objectMapper.writeValueAsString(errorResponse);

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(json);
            return;
        } else {
            // 토큰 검증 완료 되었으니 정보를 담아서 활용
            // 토큰에서 이메일 추출
            String email = jwtUtil.getEmail(token);

            // UserDetailService로 정보 조회
            UserDetails loginAdminInfo = adminDetailService.loadUserByUsername(email);

            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(
                            loginAdminInfo // principal (누구인지)
                            , null // credentials (비밀번호, 저장 하지 않는다)
                            , loginAdminInfo.getAuthorities()); // 권한 정보

            // SecurityContext에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

        // 응답
        log.info("Test Filter2 OUT");
    }

    // URI가 토큰 체크를 해야하는 URI 인지?
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        log.info("JwtFilter IN - request URI : {}",  request.getRequestURI());

        return PERMIT_ALL_PATH_ANT_STYLE.stream().anyMatch(antStyleUri -> antPathMatcher.match(antStyleUri, request.getRequestURI()));
    }
}
