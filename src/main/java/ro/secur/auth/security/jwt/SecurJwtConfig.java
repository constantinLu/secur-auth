package ro.secur.auth.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@NoArgsConstructor
@Configuration
@PropertySource("classpath:jwt.properties")
@ConfigurationProperties(prefix = "application.jwt")
public class SecurJwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
}
