package ro.secur.auth.entity;

import lombok.Getter;
import lombok.Setter;
import ro.secur.auth.common.Role;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;
}
