package com.chemical.controllers;

import com.chemical.common.BaseResponse;
import com.chemical.dto.request.RoleUpdateRequestDTO;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Role;
import com.chemical.services.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Permission Controller", description = "Endpoints for managing permissions")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PermissionController {
    private final PermissionService permissionService;

    @Operation(summary = "Get all permissions")
    @GetMapping("/get-all")
    public BaseResponse<List<PermissionResponseDTO>> getAllPermissions() {
        List<PermissionResponseDTO> permissions = permissionService.getAllPermissions();
        return BaseResponse.ok(permissions);
    }
    @Operation(summary = "Update permission details by ID")
    @PatchMapping("/update/{id}")
    public BaseResponse<Role> updatePermission(@PathVariable("id") Long id, @RequestBody RoleUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        return BaseResponse.ok();
    }
}
