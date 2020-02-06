package ro.secur.auth.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
