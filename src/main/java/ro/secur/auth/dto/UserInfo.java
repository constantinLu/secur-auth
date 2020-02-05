package ro.secur.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String aboutMe;
}
