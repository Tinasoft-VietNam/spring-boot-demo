package com.chemical.services;

import com.chemical.dto.request.RoleCreateRequestDTO;
import com.chemical.dto.request.RoleUpdateRequestDTO;
import com.chemical.dto.response.RoleResponseDTO;
import com.chemical.entity.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> getAllRoles();

    Role findById(Long roleId);

    RoleResponseDTO findDetailsById(Long roleId);

    Role save(RoleCreateRequestDTO request);

    Role update(Long roleId, RoleUpdateRequestDTO request);

    void delete(Long roleId);
}
