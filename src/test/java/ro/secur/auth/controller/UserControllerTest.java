package ro.secur.auth.controller;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ro.secur.auth.util.Api.USERS_URL;


import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.service.UserService;
import ro.secur.auth.util.Api;

import java.util.List;

class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void showAllUsers() throws  Exception {
        UserDto userLungu = UserDto.builder()
                .id(1L)
                .userName("Lungu")
                .password("pass")
                .build();

        List<UserDto> usersDto = Lists.newArrayList(userLungu);
        when(userService.getAllUsers()).thenReturn(anyList()).thenReturn(usersDto);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
    }
}