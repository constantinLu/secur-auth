package ro.secur.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ro.secur.auth.common.Role;
import ro.secur.auth.entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByRole(Role role);
}
