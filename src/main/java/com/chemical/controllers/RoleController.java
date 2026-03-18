package com.chemical.controllers;

import com.chemical.common.BaseResponse;
import com.chemical.dto.request.RoleCreateRequestDTO;
import com.chemical.dto.request.RoleUpdateRequestDTO;
import com.chemical.dto.response.RoleResponseDTO;
import com.chemical.entity.Role;
import com.chemical.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/get-all")
    public BaseResponse<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.getAllRoles();
        return BaseResponse.ok(roles);
    }

    @GetMapping("/detail/{id}")
    public BaseResponse<RoleResponseDTO> getDetailRole(@PathVariable("id") Long id) {
        RoleResponseDTO role = roleService.findDetailsById(id);
        return BaseResponse.ok(role);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Role> createRole(@Valid @RequestBody RoleCreateRequestDTO request) {
        Role savedRole = roleService.save(request);
        return BaseResponse.created(savedRole);
    }
    @PutMapping("/update/{id}")
    public BaseResponse<Role> updateRole(@PathVariable("id") Long id, @RequestBody RoleUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        Role updatedRole = roleService.update(id, request);
        return BaseResponse.ok(updatedRole);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteRole(@PathVariable("id") Long id) {
        log.info("request to update Section with id:  " + id);
        roleService.delete(id);
        return BaseResponse.ok(null);
    }

}
