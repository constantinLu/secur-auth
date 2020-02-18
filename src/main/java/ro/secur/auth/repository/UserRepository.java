package ro.secur.auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ro.secur.auth.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByUserName(String username);

    @Query("SELECT u FROM UserEntity u left join fetch u.userInfoEntity where u.userName=:username")
    UserEntity findUserInfoByUserName(@Param("username") String username);

    @Query("update UserEntity u set u.password = :password where u.userName = :username")
    void updatePassword(@Param("password") String password, @Param("username") String username);
}

