package ro.secur.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "email.configuration")
public class EmailConfiguration {

    private String emailAddress;

    private String emailPassword;

    private String host;

}
