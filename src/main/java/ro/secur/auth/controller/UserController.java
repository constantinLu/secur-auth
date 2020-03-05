package ro.secur.auth.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.requests.RegisterRequest;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.service.UserService;

import java.util.List;

/**
 * Controller used for accessing all the users or getting a specific userID.
 */
@CrossOrigin()
@RestController
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> showAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/user/{username}/password")
    public void changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(username, request);
    }

    @PostMapping("/users")
    public void register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
    }
}

