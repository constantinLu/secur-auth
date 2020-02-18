package ro.secur.auth.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.security.SecureRandom;

@Configuration
public class PasswordConfiguration {


    private static final SecureRandom RAND = new SecureRandom();

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
