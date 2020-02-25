package ro.secur.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ro.secur.auth.entity.UserInfoEntity;

public interface UserInfoRepository extends CrudRepository<UserInfoEntity, Long> {

    UserInfoEntity findByEmail(String email);
}
