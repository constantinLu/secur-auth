package ro.secur.auth.exceptions.custom;

public class EmailAlreadyExistsException extends RuntimeException {
    private static final String  message = "Email already exists ";

    public EmailAlreadyExistsException(String msg) {
        super(message + msg);
    }
}

