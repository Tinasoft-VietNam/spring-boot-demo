package com.chemical.mapper;

import com.chemical.dto.request.RoleCreateRequestDTO;
import com.chemical.dto.response.RoleResponseDTO;
import com.chemical.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {
        RolePermissionMapper.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    @Mapping(source = "rolePermissions", target = "permissions")
    RoleResponseDTO convertToRoleResponse(Role role);

    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissionKeys", ignore = true)
    Role convertRoleCreateToRole(RoleCreateRequestDTO createRequest);
}