package BtRestIa.BTRES.infrastructure.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.stereotype.Component;

@Component
public class RSAKeyManager {
    public PrivateKey loadPrivateKey() throws Exception {
        String privateKeyPem = loadKeyFromResource("/keys/private.pem");
        privateKeyPem = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = java.util.Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    public PublicKey loadPublicKey() throws Exception {
        String publicKeyPem = loadKeyFromResource("/keys/public.pem");
        publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = java.util.Base64.getDecoder().decode(publicKeyPem);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    private String loadKeyFromResource(String resourcePath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)){
            if (is == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            return new String(is.readAllBytes());
        }

    }

    public String getKeyFromResource(String resourcePath) throws Exception {
    return loadKeyFromResource(resourcePath);
}

}
