package pe.edu.upeu.AutoAndes.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.util.*;

@Service
public class JwtService {
    private final SecretKey key;
    private final Clock reloj;

    public JwtService(@Value("${app.jwt.secret}") String secret, Clock reloj) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.reloj = reloj;
    }

    public String generar(UserDetails user) {
        var ahora = reloj.instant();
        return Jwts.builder().subject(user.getUsername()).issuer("autoandes-api")
                .claim("authorities", user.getAuthorities().stream().map(a -> a.getAuthority()).toList())
                .issuedAt(Date.from(ahora)).expiration(Date.from(ahora.plusSeconds(3600))).signWith(key).compact();
    }

    public String username(String token) {
        return Jwts.parser().verifyWith(key).requireIssuer("autoandes-api").clock(() -> Date.from(reloj.instant())).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
