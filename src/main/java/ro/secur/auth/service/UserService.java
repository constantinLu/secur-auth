package ro.secur.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.secur.auth.configuration.PasswordConfiguration;
import ro.secur.auth.dto.RoleDto;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.entity.UserInfoEntity;
import ro.secur.auth.exceptions.custom.InvalidPasswordException;
import ro.secur.auth.exceptions.custom.PasswordMisMatchException;
import ro.secur.auth.exceptions.custom.UserNotFoundException;
import ro.secur.auth.repository.UserInfoRepository;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.security.password.ChangePasswordRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    private final ModelMapper modelMapper;

    private final PasswordConfiguration passwordConfiguration;

    private final EmailService emailService;

    public UserService(UserRepository userRepository, UserInfoRepository userInfoRepository, ModelMapper modelMapper, PasswordConfiguration passwordConfiguration, EmailService emailService) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.modelMapper = modelMapper;
        this.passwordConfiguration = passwordConfiguration;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUserName(username);
        if (userEntity == null) {
            throw new UserNotFoundException(username);
        }
        List<GrantedAuthority> roles = new ArrayList<>();

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        userEntity.getRoles().forEach(role -> {
            RoleDto roleDto = modelMapper.map(role, RoleDto.class);
            roles.add(new SimpleGrantedAuthority(roleDto.getRole().toString()));
        });

        return new User(userDto.getUserName(), userDto.getPassword(), roles);
    }


    public List<UserDto> getAllUsers() {
        return ((List<UserEntity>) userRepository.findAll()).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }


    public void changePassword(String username, ChangePasswordRequest request) {

        UserEntity userEntity = userRepository.findByUserName(username);
        if (userEntity == null) {
            throw new UserNotFoundException(username);
        }

        if (!passwordConfiguration.verifyHash(request.getPassword(), userEntity.getPassword())) {
            throw new InvalidPasswordException(username);
        }

        if (!request.getPassword().equals(request.getNewPassword()) &&
                request.getNewPassword().equals(request.getReTypeNewPassword())) {

            userEntity.setPassword(request.getNewPassword());
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);

            updatePassword(userDto);

        } else {
            throw new PasswordMisMatchException(String.format
                    ("New Password: %s, RetypedPassword %s", request.getNewPassword(), request.getReTypeNewPassword()));
        }
    }

    public void forgotPassword(String email, HttpServletRequest request) {

        String email1 = email.replace("{\"email\":{\"email\":\"", "");
        String email2 = email1.replace("\"}}", "");
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email2);
        UserEntity userEntity = userRepository.findByUserInfoEntity(userInfoEntity);

        if (userEntity != null) {
            userEntity.setResetToken(UUID.randomUUID().toString());

            save(userEntity);

            String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

            String token = userEntity.getResetToken();

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEntity.getUserInfoEntity().getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("secur.app.20@gmail.com");
            mailMessage.setText("To reset your password, click the link below:\n" + "http://localhost:3000/" + token + "/resetPassword");

            emailService.sendEmail(mailMessage);
        }
    }

    public void resetUserPassword(String token, ChangePasswordRequest request) {

        UserEntity userEntity = userRepository.findByResetToken(token);

        if (userEntity != null) {

            if (request.getNewPassword().equals(request.getReTypeNewPassword())) {

                userEntity.setResetToken(null);
                userEntity.setPassword(passwordConfiguration.hash(request.getNewPassword()));

                save(userEntity);

                UserDto userDto = modelMapper.map(userEntity, UserDto.class);

                resetPassword(userDto);
            }
        }
    }


    public void updatePassword(UserDto userDto) {
        userRepository.updatePassword(passwordConfiguration.hash(userDto.getPassword()), userDto.getUserName());
    }

    public void resetPassword(UserDto userDto) {
        userRepository.resetPassword(passwordConfiguration.hash(userDto.getPassword()), userDto.getResetToken());
    }
}
