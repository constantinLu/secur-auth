package ro.secur.auth.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller used for forgot password.
 */
@CrossOrigin
@RestController
public class PasswordController {

    private UserService userService;

    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody String email, HttpServletRequest request) {
        userService.forgotPassword(email, request);
    }

    @PutMapping("/{token}/resetPassword")
    public void resetPassword(@PathVariable String token, @RequestBody ChangePasswordRequest request) {
        userService.resetUserPassword(token, request);
    }
}
