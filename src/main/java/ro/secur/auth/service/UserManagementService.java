package ro.secur.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.secur.auth.dto.User;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.repository.UserDao;
import ro.secur.auth.util.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserManagementService implements UserDetailsService {

    private UserDao userDao;

    public List<User> getUsers() {
        List<User> users = ((List<UserEntity>) userDao.findAll()).stream()
                .map(m -> UserMapper.mapEntityToDto(m))
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userDao.findByUserName(userName);
        return UserMapper.mapEntityToDto(user);
    }
}
