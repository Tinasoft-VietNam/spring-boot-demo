package com.chemical.mapper;

import com.chemical.dto.request.RoleCreateRequestDTO;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.dto.response.RoleResponseDTO;
import com.chemical.entity.Role;
import com.chemical.entity.RolePermission;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class RoleMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static RoleResponseDTO convertToRoleResponse(Role role) {
        RoleResponseDTO roleResponseDTO = modelMapper.map(role, RoleResponseDTO.class);

        List<PermissionResponseDTO> permissions = role.getRolePermissions().stream()
                .map(RoleMapper::convertRolePermissionToPermissionResponseDTO)
                .collect(Collectors.toList());

        roleResponseDTO.setPermissions(permissions);
        return roleResponseDTO;
    }

    public static Role convertRoleCreateToRole(RoleCreateRequestDTO createRequest) {
        return modelMapper.map(createRequest, Role.class);
    }

    private static PermissionResponseDTO convertRolePermissionToPermissionResponseDTO(RolePermission rolePermission) {
        PermissionResponseDTO permissionDTO = new PermissionResponseDTO();
        permissionDTO.setId(rolePermission.getPermission().getId());
        permissionDTO.setTable_key(rolePermission.getPermission().getTable_key());
        permissionDTO.setIs_read(rolePermission.getIs_read());
        permissionDTO.setIs_create(rolePermission.getIs_create());
        permissionDTO.setIs_update(rolePermission.getIs_update());
        permissionDTO.setIs_delete(rolePermission.getIs_delete());
        permissionDTO.setIs_manage(rolePermission.getIs_manage());
        return permissionDTO;
    }
}