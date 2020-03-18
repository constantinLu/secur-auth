package ro.secur.auth.controller;


import static ro.secur.auth.util.Api.PREFIX_URL;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.secur.auth.security.password.ResetPasswordRequest;
import ro.secur.auth.service.UserService;


/**
 * Controller used for forgot password.
 */
@CrossOrigin
@RestController
@RequestMapping(PREFIX_URL)
public class PasswordController {

    private UserService userService;

    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "forgot password", notes = "Forgot password of a registered user")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Not authorized to change password"),
            @ApiResponse(code = 404, message = "Email not found in the database"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody String email) {
        userService.forgotPassword(email);
    }

    @ApiOperation(value = "reset password", notes = "Reset password of a registered user")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Not authorized to reset password"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/resetPassword")
    public void resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {
        userService.resetUserPassword(token, request);
    }

    @ApiOperation(value = "expired token", notes = "Token expired")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/tokenExpired")
    public Boolean isResetPasswordTokenExpired(@RequestParam String token) {
        return userService.isResetPasswordTokenExpired(token);
    }
}
