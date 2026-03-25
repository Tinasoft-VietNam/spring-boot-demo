package com.tan.userservice.services.implementations;

import com.tan.userservice.common.errors.LogicException;
import com.tan.userservice.common.errors.RecordNotFoundException;
import com.tan.userservice.dto.request.RoleCreateRequestDTO;
import com.tan.userservice.dto.request.RoleUpdateRequestDTO;
import com.tan.userservice.dto.response.RoleResponseDTO;
import com.tan.userservice.entity.Role;
import com.tan.userservice.entity.RolePermission;
import com.tan.userservice.mapper.RoleMapper;
import com.tan.userservice.repositories.RolePermissionRepository;
import com.tan.userservice.repositories.RoleRepository;
import com.tan.userservice.services.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImplementation implements RoleService {

    RoleRepository roleRepository;
    RolePermissionRepository rolePermissionRepository;
    RoleMapper roleMapper;

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::convertToRoleResponse).toList();
    }

    @Override
    public Role findById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RecordNotFoundException(" Not found role with id : " + roleId));
    }

    @Override
    public RoleResponseDTO findDetailsById(Long roleId) {
        Role role = findById(roleId);

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        role.setRolePermissions(rolePermissions);

        return roleMapper.convertToRoleResponse(role);
    }

    @Override
    public Role save(RoleCreateRequestDTO createRequest) {
        if (roleRepository.findByName(createRequest.getName()).isPresent()) {
            throw new LogicException("Role's already exist");
        }

        String nameToSlug = createRequest.getName().replaceAll(" ", "-").toLowerCase();

        if (roleRepository.findBySlug(nameToSlug).isPresent()) {
            throw new LogicException("Role name's already exist");
        }

        Role role = roleMapper.convertRoleCreateToRole(createRequest);

        role.setName(createRequest.getName());
        role.setSlug(nameToSlug);
        role.setCreated_by("user");
        role.setUpdated_by("user");
        role.setCreated_at(new Date());
        role.setUpdated_at(new Date());
        log.info("save user in service: " + role);

        return roleRepository.save(role);
    }

    @Override
    public Role update(Long roleId, RoleUpdateRequestDTO updateRequest) {
        Role role = findById(roleId);
        String nameToSlug = updateRequest.getName().replaceAll(" ", "-").toLowerCase();

        if (roleRepository.findBySlug(nameToSlug).isPresent() && !role.getSlug().equals(nameToSlug)) {
            throw new LogicException("Role name's already exist");
        }

        role.setName(updateRequest.getName());
        role.setSlug(nameToSlug);
        role.setUpdated_at(new Date());
        role.setUpdated_by("user");
        return roleRepository.save(role);
    }

    @Override
    public void delete(Long roleId) {
        Role role = findById(roleId);
        roleRepository.delete(role);
    }
}
