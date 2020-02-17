package ro.secur.auth.exceptions.custom;

import org.springframework.security.authentication.BadCredentialsException;

public class UserNotFoundException extends BadCredentialsException {

    private static final String MESSAGE = "Entity not found in the database for user: ";

    public UserNotFoundException(String user) {
        super(MESSAGE + user);
    }
}
