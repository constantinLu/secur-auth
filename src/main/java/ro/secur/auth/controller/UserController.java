package ro.secur.auth.controller;

import static ro.secur.auth.util.Api.PREFIX_URL;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping(PREFIX_URL)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "getAll users", notes = "Returns all available users in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized to see users"),
            @ApiResponse(code = 404, message = "Users not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/users")
    public List<UserDto> showAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "change password", notes = "Changes password for a user in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized to change password"),
            @ApiResponse(code = 404, message = "Users not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/user/{username}/password")
    public void changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(username, request);
    }

    @ApiOperation(value = "register user", notes = "Registers a user in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized to register user"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/users")
    public void register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
    }
}

