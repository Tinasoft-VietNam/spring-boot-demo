package com.tan.userservice.repositories;

import com.tan.userservice.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"role", "role.rolePermissions", "role.rolePermissions.permission"})
    Optional<User> findWithRolePermissionsByEmail(String email);
}
