package easy.market.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Claims getPayload(String token, String category) throws ExpiredJwtException, SignatureException {
        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        if(!payload.get("category", String.class).equals(category)) {
            throw new SignatureException("Invalid category");
        }
        return payload;
    }

    public String getUsername(Claims claims) {
       return claims.get("username", String.class);
    }
    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    public String createJwt(String category, String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}
