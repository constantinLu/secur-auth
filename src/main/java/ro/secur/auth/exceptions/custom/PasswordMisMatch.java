package ro.secur.auth.exceptions.custom;

import org.springframework.security.authentication.BadCredentialsException;

public class PasswordMisMatch extends BadCredentialsException {

    private static final String  message = "New password is not matching for current user: ";

    public PasswordMisMatch(String msg) {
        super(message + msg);
    }
}
