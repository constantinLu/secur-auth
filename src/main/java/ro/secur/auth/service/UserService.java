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
import ro.secur.auth.common.Role;
import ro.secur.auth.configuration.ForgotPasswordTokenConfiguration;
import ro.secur.auth.configuration.PasswordConfiguration;
import ro.secur.auth.dto.RoleDto;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.dto.UserInfoDto;
import ro.secur.auth.entity.RoleEntity;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.entity.UserInfoEntity;
import ro.secur.auth.exceptions.custom.EmailAlreadyExistsException;
import ro.secur.auth.exceptions.custom.InvalidEmailException;
import ro.secur.auth.exceptions.custom.InvalidPasswordException;
import ro.secur.auth.exceptions.custom.PasswordMisMatchException;
import ro.secur.auth.exceptions.custom.UserNotFoundException;
import ro.secur.auth.exceptions.custom.UsernameAlreadyExistsException;
import ro.secur.auth.repository.RoleRepository;
import ro.secur.auth.repository.UserInfoRepository;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.requests.RegisterRequest;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.security.password.ResetPasswordRequest;
import ro.secur.auth.util.DateUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final PasswordConfiguration passwordConfiguration;

    private final ForgotPasswordTokenConfiguration forgotPasswordTokenConfiguration;

    private final EmailService emailService;

    private final String EMAIL_PATTERN = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public UserService(UserRepository userRepository, UserInfoRepository userInfoRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordConfiguration passwordConfiguration, ForgotPasswordTokenConfiguration forgotPasswordTokenConfiguration, EmailService emailService) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.modelMapper = modelMapper;
        this.passwordConfiguration = passwordConfiguration;
        this.forgotPasswordTokenConfiguration = forgotPasswordTokenConfiguration;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
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

    private void save(UserEntity userEntity) {
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

            UserDto userDto = UserDto.builder()
                    .userName(username)
                    .password(request.getNewPassword())
                    .build();

            updatePassword(userDto);

        } else {
            throw new PasswordMisMatchException(String.format
                    ("New Password: %s, RetypedPassword %s", request.getNewPassword(), request.getReTypeNewPassword()));
        }
    }

    public void forgotPassword(String email) {

        String finalEmail = extractEmailAddress(email);
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(finalEmail);
        UserEntity userEntity = userRepository.findByUserInfoEntity(userInfoEntity);

        if (userEntity != null) {
            userEntity.setResetToken(createUserResetToken());
            userEntity.setTokenExpirationTime(Timestamp.valueOf((LocalDateTime.now()).plusMinutes(forgotPasswordTokenConfiguration.getTokenExpirationTime())));
            save(userEntity);
            String token = userEntity.getResetToken();
            SimpleMailMessage mailMessage = getSimpleMailMessage(userEntity, token);
            emailService.sendEmail(mailMessage);
        } else {
            throw new InvalidEmailException(String.format("Email %s", finalEmail));
        }
    }

    private String createUserResetToken() {
        boolean tokenExists = true;
        String token = UUID.randomUUID().toString();
        while (tokenExists) {
            UserEntity userEntity = userRepository.findByResetToken(token);
            if (userEntity == null) {
                tokenExists = false;
            } else {
                token = UUID.randomUUID().toString();
            }
        }
        return token;
    }

    private String extractEmailAddress(String email) {
        return email.replace("{\"email\":{\"email\":\"", "").replace("\"}}", "");
    }

    private SimpleMailMessage getSimpleMailMessage(UserEntity userEntity, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEntity.getUserInfoEntity().getEmail());
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setFrom("secur.app.20@gmail.com");
        mailMessage.setText(createEmailBody(token));
        return mailMessage;
    }

    private String createEmailBody(String token) {
        StringBuilder emailBody = new StringBuilder();
        //TODO: Get frontend URL from eureka
        emailBody.append("To reset your password, click the link below:\n")
                .append("http://localhost:3000/")
                .append("resetPassword?token=")
                .append(token)
                .append("\n")
                .append("This link will be valid only for ")
                .append(forgotPasswordTokenConfiguration.getTokenExpirationTime())
                .append(" minutes. Make sure you reset your password before that.");
        return emailBody.toString();
    }

    public void resetUserPassword(String token, ResetPasswordRequest request) {

        UserEntity userEntity = userRepository.findByResetToken(token);

        if (userEntity != null) {
            if (DateUtil.isDateInThePast(userEntity.getTokenExpirationTime())) {
                // TODO [SEC-81] Mapping BE - FE errors
                throw new RuntimeException("Reset password token is expired");
            }
            if (request.getNewPassword().equals(request.getReTypeNewPassword())) {
                userEntity.setResetToken(null);
                userEntity.setPassword(passwordConfiguration.hash(request.getNewPassword()));
                save(userEntity);
                UserDto userDto = modelMapper.map(userEntity, UserDto.class);
                resetPassword(userDto);
            } else {
                throw new PasswordMisMatchException(String.format
                        ("New Password: %s, RetypedPassword %s", request.getNewPassword(), request.getReTypeNewPassword()));
            }
        } else {
            // TODO [SEC-81] Mapping BE - FE errors
            throw new RuntimeException("Given token does not exist");
        }
    }

    public Boolean isResetPasswordTokenExpired(String token) {
        UserEntity userEntity = userRepository.findByResetToken(token);
        if (userEntity != null) {
            Timestamp tokenExpirationTime = userEntity.getTokenExpirationTime();
            return DateUtil.isDateInThePast(tokenExpirationTime);
        } else {
            // TODO [SEC-81] Mapping BE - FE errors
            throw new RuntimeException("Given token does not exist");
        }
    }

    private void updatePassword(UserDto userDto) {
        userRepository.updatePassword(passwordConfiguration.hash(userDto.getPassword()), userDto.getUserName());
    }

    private void resetPassword(UserDto userDto) {
        userRepository.resetPassword(passwordConfiguration.hash(userDto.getPassword()), userDto.getResetToken());
    }

    public void registerUser(RegisterRequest request) {

        Matcher matcher = pattern.matcher(request.getEmail());

        if (!matcher.matches()) {
            throw new InvalidEmailException(String.format("Email %s", request.getEmail()));
        }

        UserEntity user = userRepository.findByUserName(request.getUsername());

        if (user != null) {
            throw new UsernameAlreadyExistsException(String.format("UserName %s", request.getUsername()));
        }

        UserInfoEntity userInfo = userInfoRepository.findByEmail(request.getEmail());

        if (userInfo != null) {
            throw new EmailAlreadyExistsException(String.format("Email: %s", request.getEmail()));
        }

        UserDto userDto = UserDto.builder()
                .userName(request.getUsername())
                .password(passwordConfiguration.getEncoder().encode(request.getPassword()))
                .build();

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        RoleEntity roleEntity = roleRepository.findByRole(Role.USER);

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setUserInfoEntity(modelMapper.map(userInfoDto, UserInfoEntity.class));
        userEntity.setRoles(Collections.singleton(roleEntity));

        userRepository.save(userEntity);
    }
}
