package com.chemical.services.implementations;

import com.chemical.common.errors.LogicException;
import com.chemical.common.errors.RecordNotFoundException;
import com.chemical.dto.request.RoleCreateRequestDTO;
import com.chemical.dto.request.RoleUpdateRequestDTO;
import com.chemical.dto.response.PermissionResponseDTO;
import com.chemical.dto.response.RoleResponseDTO;
import com.chemical.entity.Role;
import com.chemical.entity.RolePermission;
import com.chemical.mapper.RoleMapper;
import com.chemical.repositories.RolePermissionRepository;
import com.chemical.repositories.RoleRepository;
import com.chemical.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;


    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(RoleMapper::convertToRoleResponse).toList();
    }
    @Override
    public Role findById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RecordNotFoundException(" Not found role with id : " + roleId));
    }
    @Override
    public RoleResponseDTO findDetailsById(Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        Role role = roleOptional.orElseThrow(() -> new RecordNotFoundException("Role not found with id: " + roleId));

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        role.setRolePermissions(rolePermissions);

        return RoleMapper.convertToRoleResponse(role);
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

        Role role = RoleMapper.convertRoleCreateToRole(createRequest);

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

        if (role.getId() != roleId) {
            throw new LogicException("Id is not match");
        }

        String nameToSlug = updateRequest.getName().replaceAll(" ", "-").toLowerCase();

        if (roleRepository.findBySlug(nameToSlug).isPresent()) {
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
        try {
            roleRepository.deleteById(roleId);
        } catch (Exception e) {
            log.debug("Delete role " + e.getMessage());
            throw new LogicException("Unknown error");
        }
    }
}
