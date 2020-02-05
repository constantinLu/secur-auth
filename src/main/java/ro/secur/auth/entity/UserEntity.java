package ro.secur.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFO_ID")
    private UserInfoEntity userInfoEntity;

    @OneToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity role;

}
