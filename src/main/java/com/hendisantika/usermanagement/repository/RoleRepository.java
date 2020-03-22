package com.hendisantika.usermanagement.repository;

import com.hendisantika.usermanagement.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 22/03/20
 * Time: 10.04
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String role);
}