package com.chemical.controllers;

import com.chemical.common.BaseResponse;
import com.chemical.dto.request.RoleUpdateRequestDTO;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.entity.Role;
import com.chemical.services.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping("/get-all")
    public BaseResponse<List<PermissionResponseDTO>> getAllPermissions() {
        List<PermissionResponseDTO> permissions = permissionService.getAllPermissions();
        return BaseResponse.ok(permissions);
    }
    @PatchMapping("/update/{id}")
    public BaseResponse<Role> updatePermission(@PathVariable("id") Long id, @RequestBody RoleUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        return BaseResponse.ok();
    }
}
