package ro.secur.auth.controller;

import org.apache.catalina.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.entity.UserInfoEntity;
import ro.secur.auth.repository.UserInfoRepository;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.service.EmailService;
import ro.secur.auth.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

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
