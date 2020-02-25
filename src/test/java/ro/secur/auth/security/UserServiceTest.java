package ro.secur.auth.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ro.secur.auth.common.Role;
import ro.secur.auth.entity.RoleEntity;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.exceptions.custom.UserNotFoundException;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepoMock;

    private UserService userService;

    private User expectedUser;

    @BeforeEach
    public void init() {
        userService = new UserService(userRepoMock, new ModelMapper());
    }

    private void mockUserRepoReturnsUser(String username, String password, Role role) {

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setRole(role);

        UserEntity user = new UserEntity();
        user.setUserName(username);
        user.setPassword(password);
        user.setRoles(Collections.singleton(roleEntity));

        when(userRepoMock.findByUserName(any(String.class))).thenReturn(user);
    }

    private void mockUserRepoReturnsNull() {
        when(userRepoMock.findByUserName(any(String.class))).thenReturn(null);
    }

    @Test
    public void whenLoadUserByUsername_returnUser() {

        mockUserRepoReturnsUser("test", "pass", Role.USER);

        expectedUser = new User("test", "pass",
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.name())));
        User actualUser = (User) userService.loadUserByUsername("test");

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void whenLoadUserByUsername_throwException() {

        mockUserRepoReturnsNull();

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.loadUserByUsername("test"));

        String expectedMessage = "Entity not found in the database for user: test";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}