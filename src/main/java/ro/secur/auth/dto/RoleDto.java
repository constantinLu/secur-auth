package ro.secur.auth.dto;

import lombok.Data;
import ro.secur.auth.common.Role;

@Data
public class RoleDto {

    private String id;

    private Role role;
}
