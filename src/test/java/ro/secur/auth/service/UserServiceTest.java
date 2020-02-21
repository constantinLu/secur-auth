package ro.secur.auth.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import ro.secur.auth.configuration.PasswordConfiguration;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.exceptions.custom.InvalidPasswordException;
import ro.secur.auth.exceptions.custom.PasswordMisMatchException;
import ro.secur.auth.repository.UserRepository;
import ro.secur.auth.security.password.ChangePasswordRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordConfiguration passwordConfiguration;

    private UserService userService;


    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, modelMapper, passwordConfiguration);
    }

    UserServiceTest() {
        this.modelMapper = new ModelMapper();
        this.passwordConfiguration = new PasswordConfiguration();
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

        List<UserEntity> receivedEntities = (List<UserEntity>) userRepository.findAll();

        //then
        assertEquals(receivedEntities.size(), 1);
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void changePassword() {
        //given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("BruceLee")
                .password("kongfuuuuu")
                .build();
        //when
        when(userRepository.findByUserName(any(String.class))).thenReturn(userEntity);
        String cryptedPassword = passwordConfiguration.hash(userEntity.getPassword());

        //then
        assertEquals(userEntity.getPassword(), "kongfuuuuu");
        assertTrue(passwordConfiguration.verifyHash("kongfuuuuu", cryptedPassword));
    }

    @Test
    void checkPassword_thenThrowInvalidPasswordEx() {
        //given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .username("lungu")
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
        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(request));

    }

    @Test
    void checkPassword_thenThrowMismatchEx() {
        //given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .username("Ed")
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
        assertThrows(PasswordMisMatchException.class, () -> userService.changePassword(request));

    }
}