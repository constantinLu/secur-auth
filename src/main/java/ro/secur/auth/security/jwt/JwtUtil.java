package ro.secur.auth.security.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtUtil {

    private String secretKey;

    private String tokenPrefix;

    private Integer tokenExpirationDays;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

}
