package ro.secur.auth.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.secur.auth.common.Role;
import ro.secur.auth.entity.RoleEntity;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {

        userService = new UserService(userRepository, new ModelMapper());

        user = new User("test", "pass", Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    private void mockUserRepository() {

        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setRole(Role.USER);

        UserEntity user = new UserEntity();
        user.setUserName("test");
        user.setPassword("pass");
        user.setRoles(Collections.singleton(role));

        when(userRepository.findByUserName(any(String.class))).thenReturn(user);
    }

    @Test
    public void wheLoadUserByUsername_returnUser() {

        mockUserRepository();
        User result = (User) userService.loadUserByUsername("test");

        Assertions.assertEquals(user, result);
    }

    @Test
    public void wheLoadUserByUsername_throwException() {

        when(userRepository.findByUserName(any(String.class))).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test"));
    }
}
