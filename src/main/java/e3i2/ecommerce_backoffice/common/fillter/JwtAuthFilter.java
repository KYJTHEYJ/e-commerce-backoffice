package e3i2.ecommerce_backoffice.common.fillter;

import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.common.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Order(3)
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청
        log.info("JwtAuthFilter IN");

        // URI가 토큰 체크를 해야하는 URI 인지?
        String requestURI = request.getRequestURI();
        if(requestURI.equals("/api/auth/login_NEW")) { // 로그인 시도는 통과
            filterChain.doFilter(request, response);
            return;
        }

        // 올바른 토큰이 있는지?
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
        authorization = authorization.substring("Bearer ".length());

        if(!jwtUtil.validateToken(authorization)) {
            MessageResponse errorResponse = MessageResponse.fail(HttpStatus.UNAUTHORIZED.name(),  "인증 정보에 대한 확인이 필요합니다");
            String json = objectMapper.writeValueAsString(errorResponse);

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(json);
            return;
        } else {
            request.setAttribute("email", request.getAttribute("email"));
        }

        filterChain.doFilter(request, response);

        // 응답
        log.info("Test Filter2 OUT");
    }
}
