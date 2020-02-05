package ro.secur.auth.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String aboutMe;
}
