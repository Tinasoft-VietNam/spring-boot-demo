package com.tan.userservice.services;

import com.tan.userservice.dto.request.RoleCreateRequestDTO;
import com.tan.userservice.dto.request.RoleUpdateRequestDTO;
import com.tan.userservice.dto.response.RoleResponseDTO;
import com.tan.userservice.entity.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> getAllRoles();

    Role findById(Long roleId);

    RoleResponseDTO findDetailsById(Long roleId);

    Role save(RoleCreateRequestDTO request);

    Role update(Long roleId, RoleUpdateRequestDTO request);

    void delete(Long roleId);
}
