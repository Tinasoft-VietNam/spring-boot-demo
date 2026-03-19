package com.chemical.mapper;

import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
        @Mapping(target = "is_read", ignore = true)
        @Mapping(target = "is_create", ignore = true)
        @Mapping(target = "is_update", ignore = true)
        @Mapping(target = "is_delete", ignore = true)
        @Mapping(target = "is_manage", ignore = true)
        PermissionResponseDTO toPermissionResponse(Permission permission);
}
