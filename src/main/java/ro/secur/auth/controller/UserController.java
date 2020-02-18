package ro.secur.auth.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.service.UserService;

import java.util.List;

/**
 * Controller used for accessing all the users or getting a specific userID.
 */
@RestController
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> listUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/password")
    public void changePassword(@RequestBody UserDto user) {
        userService.savePassword(user);
    }
}

