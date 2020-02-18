package ro.secur.auth.security.password;

import lombok.Getter;
import lombok.Setter;

import javax.naming.directory.SearchResult;

@Getter
@Setter
public class ChangePasswordRequest{

    private String username;

    private String password;

    private String newPassword;

    private String reTypeNewPassword;


}
