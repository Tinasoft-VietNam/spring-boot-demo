package com.chemical.services.implementations;

import com.chemical.common.errors.RecordNotFoundException;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Permission;
import com.chemical.entity.RolePermission;
import com.chemical.mapper.PermissionMapper;
import com.chemical.repositories.PermissionRepository;
import com.chemical.repositories.RolePermissionRepository;
import com.chemical.services.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionServiceImplementation implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .flatMap(permission -> getPermissionDetailsById(permission.getId()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionResponseDTO> getPermissionDetailsById(Long permissionId) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findByPermissionId(permissionId);
        if (rolePermissions.isEmpty()) {
            throw new RecordNotFoundException("Permission not found with id: " + permissionId);
        }

        return rolePermissions.stream()
                .map(PermissionMapper::convertToPermissionResponse)
                .collect(Collectors.toList());
    }
}
