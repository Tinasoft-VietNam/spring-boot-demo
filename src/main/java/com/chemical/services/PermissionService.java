package com.chemical.services;

import com.chemical.dto.response.PermissionResponseDTO;

import java.util.List;

public interface PermissionService {
    List<PermissionResponseDTO> getAllPermissions();

    List<PermissionResponseDTO> getPermissionDetailsById(Long permissionId);

}
