package ro.secur.auth.exceptions.custom;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {
    private static final String  message = "Invalid Authentication for: ";


    public AuthException(String msg) {
        super(message + msg);
    }

}
