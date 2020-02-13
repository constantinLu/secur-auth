package ro.secur.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.secur.auth.dto.RoleDto;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.entity.RoleEntity;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> roles = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserName(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Username could not be found in the database: " + username);
        }

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);

        for (RoleEntity roleEntity : userEntity.getRoles()) {
            RoleDto roleDto = modelMapper.map(roleEntity, RoleDto.class);
            roles.add(new SimpleGrantedAuthority(roleDto.getRole().toString()));
        }

        return new User(userDto.getUserName(), userDto.getPassword(), roles);
    }

    public List<UserDto> getAllUsers() {
        return ((List<UserEntity>) userRepository.findAll()).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }
}

