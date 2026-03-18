package com.chemical.mapper;

import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Permission;
import com.chemical.entity.RolePermission;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.modelmapper.ModelMapper;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
PermissionResponseDTO toPermissionResponse(Permission permission);
}
