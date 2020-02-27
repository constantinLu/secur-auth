package ro.secur.auth.controller;

import org.springframework.web.bind.annotation.*;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.service.UserService;

import javax.servlet.http.HttpServletRequest;

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
