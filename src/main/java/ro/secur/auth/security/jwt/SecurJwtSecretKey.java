package ro.secur.auth.security.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;

@Configuration
public class SecurJwtSecretKey {

    private final SecurJwtConfig securJwtConfig;

    SecurJwtSecretKey(SecurJwtConfig securJwtConfig){
        this.securJwtConfig = securJwtConfig;
    }

    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(securJwtConfig.getSecretKey().getBytes());
    }
}
