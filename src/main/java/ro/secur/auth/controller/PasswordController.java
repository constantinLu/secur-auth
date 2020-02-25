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
import ro.secur.auth.service.EmailService;
import ro.secur.auth.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestController
public class PasswordController {

    private UserService userService;

    private EmailService emailService;

    private UserRepository userRepository;

    private UserInfoRepository userInfoRepository;


    public PasswordController(UserService userService, EmailService emailService, UserRepository userRepository,
                              UserInfoRepository userInfoRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody String email, HttpServletRequest request) {

        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);
        UserEntity userEntity = userRepository.findByUserInfoEntity(userInfoEntity);

        if (userEntity != null) {

            userEntity.setResetToken(UUID.randomUUID().toString());

            userService.save(userEntity);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEntity.getUserInfoEntity().getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("secur.app.20@gmail.com");
            mailMessage.setText("To reset your password, click the link below:\n" + appUrl + "/reset?token=" + userEntity.getResetToken());

            emailService.sendEmail(mailMessage);
        }
    }
}
