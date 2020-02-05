package ro.secur.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.repository.UserRepository;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Username could not be found in the database: " + username);
        }
        return new User(userEntity.getUserName(), userEntity.getPassword(), new ArrayList<>());
    }
}

