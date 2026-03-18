package com.chemical.mapper;

import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.RolePermission;
import org.modelmapper.ModelMapper;

public class PermissionMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    public static PermissionResponseDTO convertToPermissionResponse(RolePermission rolePermission) {
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
