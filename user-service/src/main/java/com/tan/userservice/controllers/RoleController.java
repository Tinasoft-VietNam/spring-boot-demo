package com.tan.userservice.controllers;

import com.tan.userservice.common.BaseResponse;
import com.tan.userservice.dto.request.RoleCreateRequestDTO;
import com.tan.userservice.dto.request.RoleUpdateRequestDTO;
import com.tan.userservice.dto.response.RoleResponseDTO;
import com.tan.userservice.entity.Role;
import com.tan.userservice.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_READ')")
    public BaseResponse<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.getAllRoles();
        return BaseResponse.ok(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_READ')")
    public BaseResponse<RoleResponseDTO> getDetailRole(@PathVariable("id") Long id) {
        RoleResponseDTO role = roleService.findDetailsById(id);
        return BaseResponse.ok(role);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CREATE', 'ROLE_MANAGE')")
    public BaseResponse<Role> createRole(@Valid @RequestBody RoleCreateRequestDTO request) {
        Role savedRole = roleService.save(request);
        return BaseResponse.created(savedRole);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_UPDATE', 'ROLE_MANAGE')")
    public BaseResponse<Role> updateRole(@PathVariable("id") Long id, @RequestBody RoleUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        Role updatedRole = roleService.update(id, request);
        return BaseResponse.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DELETE', 'ROLE_MANAGE')")
    public BaseResponse<Void> deleteRole(@PathVariable("id") Long id) {
        log.info("request to update Section with id:  " + id);
        roleService.delete(id);
        return BaseResponse.ok(null);
    }

}
