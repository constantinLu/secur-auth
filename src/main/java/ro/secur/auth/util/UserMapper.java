package ro.secur.auth.util;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ro.secur.auth.common.Role;
import ro.secur.auth.dto.User;
import ro.secur.auth.dto.UserInfo;
import ro.secur.auth.entity.RoleEntity;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.entity.UserInfoEntity;

public class UserMapper {

    public static User mapEntityToDto(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUserName());
        user.setPassword(userEntity.getPassword());
        user.setRole(userEntity.getRoleEntity().getRole());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setGrantedAuthorities(Sets.newHashSet(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

        return user;
    }

    public static UserEntity mapDtoToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setUserInfoEntity(mapUserInfoEntity(user.getUserInfo()));
        userEntity.setRoleEntity(mapRoleEntity(user.getRole()));

        return userEntity;
    }

    private static RoleEntity mapRoleEntity(Role role) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(role);

        return roleEntity;
    }

    private static UserInfoEntity mapUserInfoEntity(UserInfo userInfo) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setFirstName(userInfo.getFirstName());
        userInfoEntity.setLastName(userInfo.getLastName());
        userInfoEntity.setEmail(userInfo.getEmail());
        userInfoEntity.setAboutMe(userInfo.getAboutMe());

        return  userInfoEntity;
    }

}
