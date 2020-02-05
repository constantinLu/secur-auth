package ro.secur.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {

    private Long id;

    private ro.secur.auth.common.Role role;
}
