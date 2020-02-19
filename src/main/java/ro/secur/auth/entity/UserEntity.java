package ro.secur.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

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


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private Set<RoleEntity> roles;
}
