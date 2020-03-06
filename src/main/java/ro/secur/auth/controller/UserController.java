package ro.secur.auth.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.secur.auth.dto.UserDto;
import ro.secur.auth.requests.RegisterRequest;
import ro.secur.auth.security.password.ChangePasswordRequest;
import ro.secur.auth.service.UserService;

import java.util.List;

/**
 * Controller used for accessing all the users or getting a specific userID.
 */
@CrossOrigin
@RestController
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

