package ro.secur.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ro.secur.auth.entity.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);
}
