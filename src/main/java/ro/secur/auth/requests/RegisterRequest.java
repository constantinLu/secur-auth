package ro.secur.auth.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;
}
