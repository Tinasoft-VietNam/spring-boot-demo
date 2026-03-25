package com.tan.userservice.common;

import com.tan.userservice.entity.Permission;
import com.tan.userservice.entity.Role;
import com.tan.userservice.entity.RolePermission;
import com.tan.userservice.entity.User;
import com.tan.userservice.repositories.PermissionRepository;
import com.tan.userservice.repositories.RolePermissionRepository;
import com.tan.userservice.repositories.RoleRepository;
import com.tan.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RbacDataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = createIfAbsentRole("ADMIN", "admin");
        Role userRole = createIfAbsentRole("USER", "user");

        Permission userPermission = createIfAbsentPermission("USER");
        Permission rolePermission = createIfAbsentPermission("ROLE");
        Permission permissionPermission = createIfAbsentPermission("PERMISSION");
        Permission profilePermission = createIfAbsentPermission("PROFILE");

        grant(adminRole, userPermission, 1, 1, 1, 1, 1);
        grant(adminRole, rolePermission, 1, 1, 1, 1, 1);
        grant(adminRole, permissionPermission, 1, 1, 1, 1, 1);
        grant(adminRole, profilePermission, 1, 1, 1, 1, 1);

        grant(userRole, userPermission, 1, 0, 0, 0, 0);
        grant(userRole, profilePermission, 1, 0, 1, 0, 0);

        createIfAbsentUser("Admin", "admin@users.local", "Admin@123", adminRole);
        createIfAbsentUser("User", "user@users.local", "User@123", userRole);

        log.info("RBAC seed completed: roles, permissions, role-permissions, and demo users are ready.");
    }

    private Role createIfAbsentRole(String name, String slug) {
        return roleRepository.findBySlug(slug)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setSlug(slug);
                    role.setCreated_by("system");
                    role.setUpdated_by("system");
                    role.setCreated_at(new Date());
                    role.setUpdated_at(new Date());
                    return roleRepository.save(role);
                });
    }

    private Permission createIfAbsentPermission(String tableKey) {
        return permissionRepository.findByTableKey(tableKey)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setTable_key(tableKey);
                    permission.setCreated_by("system");
                    permission.setUpdated_by("system");
                    permission.setCreated_at(new Date());
                    permission.setUpdated_at(new Date());
                    return permissionRepository.save(permission);
                });
    }

    private void grant(Role role, Permission permission, int canRead, int canCreate, int canUpdate, int canDelete,
            int canManage) {
        if (rolePermissionRepository.findByRoleIdAndPermissionId(role.getId(), permission.getId()).isPresent()) {
            return;
        }

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        rolePermission.setIs_read(canRead);
        rolePermission.setIs_create(canCreate);
        rolePermission.setIs_update(canUpdate);
        rolePermission.setIs_delete(canDelete);
        rolePermission.setIs_manage(canManage);
        rolePermission.setCreated_by("system");
        rolePermission.setUpdated_by("system");
        rolePermission.setCreated_at(new Date());
        rolePermission.setUpdated_at(new Date());

        rolePermissionRepository.save(rolePermission);
    }

    private void createIfAbsentUser(String name, String email, String rawPassword, Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setCreated_by("system");
        user.setUpdated_by("system");
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());

        userRepository.save(user);
    }
}
