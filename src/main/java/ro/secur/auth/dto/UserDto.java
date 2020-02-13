package ro.secur.auth.dto;


import lombok.Data;

@Data
public class UserDto {

    private String id;

    private String userName;

    private String password;
}
