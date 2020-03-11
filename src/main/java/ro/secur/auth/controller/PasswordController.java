package ro.secur.auth.controller;

import org.springframework.web.bind.annotation.*;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.service.UserService;

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
    public void forgotPassword(@RequestBody String email) {
        userService.forgotPassword(email);
    }

    @PutMapping("/resetPassword")
    public void resetPassword(@RequestParam String token, @RequestBody ChangePasswordRequest request) {
        userService.resetUserPassword(token, request);
    }

    @GetMapping("/tokenExpired")
    public Boolean isResetPasswordTokenExpired(@RequestParam String token) {
        return userService.isResetPasswordTokenExpired(token);
    }
}
