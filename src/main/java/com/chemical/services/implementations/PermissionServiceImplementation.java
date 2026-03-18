package com.chemical.services.implementations;

import com.chemical.common.errors.RecordNotFoundException;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Permission;
import com.chemical.entity.RolePermission;
import com.chemical.mapper.PermissionMapper;
import com.chemical.mapper.RolePermissionMapper;
import com.chemical.repositories.PermissionRepository;
import com.chemical.repositories.RolePermissionRepository;
import com.chemical.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<RolePermission> rolePermissions =
                rolePermissionRepository.findByPermissionId(permissionId);

        if (rolePermissions.isEmpty()) {
            throw new RecordNotFoundException(
                    "Permission not found with id: " + permissionId
            );
        }

        return rolePermissionMapper.toPermissionResponseList(rolePermissions);
    }
}
