package com.tan.userservice.services;

import com.tan.userservice.dto.response.PermissionResponseDTO;

import java.util.List;

public interface PermissionService {
    List<PermissionResponseDTO> getAllPermissions();

    List<PermissionResponseDTO> getPermissionDetailsById(Long permissionId);

}
