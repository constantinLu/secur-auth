package ro.secur.auth.exceptions.custom;

public class InvalidPasswordException extends  RuntimeException {

    private static final String  message = "Invalid password for selected user:";

    public InvalidPasswordException(String msg) {
        super(message + msg);
    }
}
