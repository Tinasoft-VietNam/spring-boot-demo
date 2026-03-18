package com.chemical.mapper;

import com.chemical.dto.request.RoleCreateRequestDTO;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.dto.response.RoleResponseDTO;
import com.chemical.entity.Role;
import com.chemical.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    RoleResponseDTO convertToRoleResponse(Role role);
    Role convertRoleCreateToRole(RoleCreateRequestDTO createRequest);
}