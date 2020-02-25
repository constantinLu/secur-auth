package ro.secur.auth.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.secur.auth.entity.UserEntity;
import ro.secur.auth.entity.UserInfoEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByUserName(String username);

    UserEntity findByResetToken(String resetToken);

    @Query("SELECT u FROM UserEntity u left join fetch u.userInfoEntity where u.userName=:username")
    UserEntity findUserInfoByUserName(@Param("username") String username);

    UserEntity findByUserInfoEntity(@Param("userInfoEntity") UserInfoEntity userInfoEntity);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.password = :password where u.userName = :username")
    void updatePassword(@Param("password") String password, @Param("username") String username);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.password = :password where u.resetToken = :resetToken")
    void resetPassword(@Param("password") String password, @Param("resetToken") String resetToken);

}

