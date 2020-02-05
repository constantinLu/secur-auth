package ro.secur.auth.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponse {

    private final String jwt;

}
