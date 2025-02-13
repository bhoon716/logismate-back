package hangman.logismate.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final SecretKey signingKey;

    public JwtUtil(@Value("${jwt.secretKey}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성 (예: 로그인 시)
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // subject에 userId 저장
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24시간 후 만료
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // HTTP 요청에서 JWT 토큰 추출 ("Bearer " 제거)
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    // 요청에서 userId 추출 (문제가 있을 경우 null 반환)
    public Long getUserIdFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return null;
        }
        return getUserIdFromToken(token);
    }

    public Long getUserIdFromToken(String token) {
        try {
            String subject = getClaimFromToken(token, Claims::getSubject);
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            System.err.println("JWT Subject 변환 오류: " + e.getMessage());
            return null;
        }
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("JWT 검증 실패: " + e.getMessage());
            return false;
        }
    }
}
