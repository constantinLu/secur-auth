package ro.secur.auth.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.secur.auth.security.authentication.AuthService;
import ro.secur.auth.security.authentication.AuthenticationRequest;
import ro.secur.auth.security.authentication.AuthenticationResponse;

@RestController
@RequestMapping("/api/v1")
@Slf4j
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
            String infoMessage = "Incorrect username or password";
            log.info(infoMessage);
            throw new Exception(infoMessage, e);
        }

        String token = authService.generateToken(authentication);

        return new AuthenticationResponse(token);
    }
}
