package cashflow.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(String username, List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("roles cannot be empty");
        }
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}


//.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
