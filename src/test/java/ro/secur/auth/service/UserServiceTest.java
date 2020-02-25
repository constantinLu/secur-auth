package ro.secur.auth.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ro.secur.auth.common.Role;
import ro.secur.auth.configuration.PasswordConfiguration;
import ro.secur.auth.entity.RoleEntity;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.exceptions.custom.InvalidPasswordException;
import ro.secur.auth.exceptions.custom.PasswordMisMatchException;
import ro.secur.auth.exceptions.custom.UserNotFoundException;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.security.password.ChangePasswordRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordConfiguration passwordConfiguration;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new ModelMapper(), passwordConfiguration);
    }


    void mockUserRepoReturnsUser(String username, String password, Role role) {

        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .role(role)
                .build();

        UserEntity user = UserEntity.builder()
                .id(1L)
                .userName(username)
                .password(password)
                .roles(Collections.singleton(roleEntity))
                .build();

        when(userRepository.findByUserName(any(String.class))).thenReturn(user);
    }

    void mockUserRepoReturnsNull() {
        when(userRepository.findByUserName(any(String.class))).thenReturn(null);
    }


    @Test
    void whenLoadUserByUsername_returnUser() {

        mockUserRepoReturnsUser("test", "pass", Role.USER);

        User expectedUser = new User("test", "pass",
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.name())));
        User actualUser = (User) userService.loadUserByUsername("test");

        Assertions.assertEquals(expectedUser, actualUser);
    }


    @Test
    void whenLoadUserByUsername_throwException() {

        mockUserRepoReturnsNull();

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.loadUserByUsername("test"));

        String expectedMessage = "Entity not found in the database for user: test";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void getAllUsers() {
        //given
        UserEntity user = UserEntity.builder()
                .id(3L)
                .userName("TOM-MAC-BILL-BOB-CONSTANTIN")
                .password("marcel")
                .build();
        List<UserEntity> userEntities = Lists.newArrayList(user);

        //when
        when(userRepository.findAll()).thenReturn(userEntities);

        //then
        assertEquals(userService.getAllUsers().size(), 1);
    }


    @Test
    void changePassword_thenThrowInvalidPasswordEx() {
        //given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("JohnnyBravo")
                .newPassword("JohnnyBravo")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("BruceLee")
                .password("kongfuuuuu")
                .build();

        //when
        when(userRepository.findByUserName(any(String.class))).thenReturn(userEntity);
        when(passwordConfiguration.verifyHash(anyString(), anyString())).thenReturn(false);

        //then
        assertThrows(InvalidPasswordException.class, () -> userService.changePassword("lungu", request));

    }


    @Test
    void changePassword_thenThrowMismatchEx() {
        //given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("Edd")
                .newPassword("nEddy")
                .reTypeNewPassword("!")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("Ed")
                .password("Edd")
                .build();
        //when
        when(userRepository.findByUserName(any(String.class))).thenReturn(userEntity);
        when(passwordConfiguration.verifyHash(request.getPassword(), userEntity.getPassword())).thenReturn(true);

        //then
        assertThrows(PasswordMisMatchException.class, () -> userService.changePassword("Ed", request));

    }
}