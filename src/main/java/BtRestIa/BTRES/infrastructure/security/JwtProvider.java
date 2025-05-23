package BtRestIa.BTRES.infrastructure.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private final RSAKeyManager rsaKeyManager;
    private static final long EXPIRATION_TIME = 36000000; // 1 hora 

    public JwtProvider(RSAKeyManager rsaKeyManager) {
        this.rsaKeyManager = rsaKeyManager;
    }

    private PublicKey getPublicKey() {
        try {
            return rsaKeyManager.loadPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Error carga public key", e);
        }

    }

    private PrivateKey getPrivateKey() {
        try {
            return rsaKeyManager.loadPrivateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la clave privada", e);
        }

    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(token);
    }

    // Generar un JWT con RSA256
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    // validar un JWT con la clave publica
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalido JWT token: {}", e.getMessage());
            return false;
        }
    }

    // Extraer el mail(sub)
    public String getEmailFromToken(String token) {
        return getClaims(token).getPayload().getSubject();
    }

    // Extraer el rol
    public String getRoleFromToken(String token) {
        return getClaims(token).getPayload().get("role", String.class);

    }

}
