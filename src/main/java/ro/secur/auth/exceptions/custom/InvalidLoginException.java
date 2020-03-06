package ro.secur.auth.exceptions.custom;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidLoginException {

    private String username;
    private String password;

    public InvalidLoginException() {
        this.username = "Invalid username";
        this.password = "Invalid password";
    }
}
