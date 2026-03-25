package com.tan.userservice.services.implementations;

import com.tan.userservice.common.errors.RecordNotFoundException;
import com.tan.userservice.dto.response.PermissionResponseDTO;
import com.tan.userservice.entity.RolePermission;
import com.tan.userservice.mapper.RolePermissionMapper;
import com.tan.userservice.repositories.PermissionRepository;
import com.tan.userservice.repositories.RolePermissionRepository;
import com.tan.userservice.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImplementation implements PermissionService {
    PermissionRepository permissionRepository;
    RolePermissionRepository rolePermissionRepository;
    RolePermissionMapper rolePermissionMapper;

    @Override
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .flatMap(permission -> getPermissionDetailsById(permission.getId()).stream())
                .toList();
    }

    @Override
    public List<PermissionResponseDTO> getPermissionDetailsById(Long permissionId) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findByPermissionId(permissionId);

        if (rolePermissions.isEmpty()) {
            throw new RecordNotFoundException(
                    "Permission not found with id: " + permissionId);
        }

        return rolePermissionMapper.toPermissionResponseList(rolePermissions);
    }
}
