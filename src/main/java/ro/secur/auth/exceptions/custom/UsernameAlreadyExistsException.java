package ro.secur.auth.exceptions.custom;

public class UsernameAlreadyExistsException extends RuntimeException {
    private static final String  message = "Username already exists ";

    public UsernameAlreadyExistsException(String msg) {
        super(message + msg);
    }
}
