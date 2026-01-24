package e3i2.ecommerce_backoffice.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    // 만료 시간 설정
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    // 설정한 비밀키를 가져오기
    @Value("${jwt.secret.key}") // application.yml 에 있는 key 가져옴
    private String secretKeyString;

    private SecretKey key;
    private JwtParser parser;

    // 서버가 실행시 가장 먼저 생성할 것을 명시 하는 어노테이션 @PostConsturct
    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(bytes); // 설정된 비밀키의 바이트 배열을 전달 해주면 HMAC-SHA 알고리즘을 통해 SecretKey 타입의 비밀키를 만들어 준다
        this.parser = Jwts.parser()
                .verifyWith(this.key)
                .build();
    }

    // 토큰 생성 (순수 JWT 토큰만 반환, Bearer prefix는 프론트에서 추가 (지금은 Postman 활용)
    public String generateToken(String adminEmail) {
        Date now = new Date();
        return Jwts.builder()
                .subject(adminEmail) // 토큰을 받을 주체
                .claim("adminEmail", adminEmail) // 다양한 정보를 담을 수 있는 빈 객체 (claim)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TOKEN_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) return false; // 토큰이 비었는지 검사
        try {
            parser.parseSignedClaims(token); // 토큰이 서명키로 파싱, 불가시 exception
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 예외처리를 상세히 하지 않는 것이 공격자에게 보안적으로 좋다 (단순한 false 구분값 return)
            // 내부 로그엔 모든 JWT 관련 오류인 JwtException 으로 로깅
            log.debug("Invalid JWT: {}", e.toString());
            return false;
        }
    }

    // 토큰 복호화
    private Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload(); // 토큰을 서명키로 파싱, 그 후 페이로드 반환
    }

    public String getEmail(String token) {
        return extractAllClaims(token).get("adminEmail", String.class);
    }
}
