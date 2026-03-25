package com.tan.userservice.controllers;

import com.tan.userservice.common.BaseResponse;
import com.tan.userservice.dto.request.RoleUpdateRequestDTO;
import com.tan.userservice.dto.response.PermissionResponseDTO;
import com.tan.userservice.entity.Role;
import com.tan.userservice.services.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'PERMISSION_READ')")
    public BaseResponse<List<PermissionResponseDTO>> getAllPermissions() {
        List<PermissionResponseDTO> permissions = permissionService.getAllPermissions();
        return BaseResponse.ok(permissions);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'PERMISSION_UPDATE', 'PERMISSION_MANAGE')")
    public BaseResponse<Role> updatePermission(@PathVariable("id") Long id, @RequestBody RoleUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        return BaseResponse.ok();
    }
}
