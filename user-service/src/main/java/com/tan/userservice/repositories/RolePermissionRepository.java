package com.tan.userservice.repositories;

import com.tan.userservice.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository
        extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {
    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);

    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

}
