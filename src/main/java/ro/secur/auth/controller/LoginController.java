package ro.secur.auth.controller;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.secur.auth.security.authentication.AuthenticationRequest;
import ro.secur.auth.security.authentication.AuthenticationResponse;
import ro.secur.auth.security.jwt.AuthService;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    private AuthenticationResponse login(@RequestBody AuthenticationRequest request) throws Exception {

        Authentication authentication;
        try {
            authentication = authService.authenticate(request);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        String token = authService.generateToken(authentication);

        return new AuthenticationResponse(token);
    }
}
