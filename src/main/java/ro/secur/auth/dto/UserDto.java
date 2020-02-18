package ro.secur.auth.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String id;

    private String userName;

    private String password;
}
