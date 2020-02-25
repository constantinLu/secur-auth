package ro.secur.auth.service;

import org.modelmapper.ModelMapper;
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
import ro.secur.auth.exceptions.custom.InvalidPasswordException;
import ro.secur.auth.exceptions.custom.PasswordMisMatchException;
import ro.secur.auth.exceptions.custom.UserNotFoundException;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.security.password.ChangePasswordRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordConfiguration passwordConfiguration;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordConfiguration passwordConfiguration) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordConfiguration = passwordConfiguration;
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


    public void updatePassword(UserDto userDto) {
        userRepository.updatePassword(passwordConfiguration.hash(userDto.getPassword()), userDto.getUserName());
    }
}
