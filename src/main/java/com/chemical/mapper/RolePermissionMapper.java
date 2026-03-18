package com.chemical.mapper;

import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Permission;
import com.chemical.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RolePermissionMapper {
    @Mapping(source = "permission.id", target = "id")
    @Mapping(source = "permission.table_key", target = "table_key")
    @Mapping(source = "is_read", target = "is_read")
    @Mapping(source = "is_create", target = "is_create")
    @Mapping(source = "is_update", target = "is_update")
    @Mapping(source = "is_delete", target = "is_delete")
    @Mapping(source = "is_manage", target = "is_manage")
    PermissionResponseDTO toPermissionResponse(RolePermission rolePermission);

    List<PermissionResponseDTO> toPermissionResponseList(List<RolePermission> rolePermissionList);
}
