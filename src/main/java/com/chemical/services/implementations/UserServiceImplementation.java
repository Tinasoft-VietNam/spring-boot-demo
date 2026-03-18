package com.chemical.services.implementations;

import com.chemical.common.errors.ConflictException;
import com.chemical.common.errors.LogicException;
import com.chemical.common.errors.RecordNotFoundException;
import com.chemical.common.query.SearchRequest;
import com.chemical.common.query.SearchSpecification;
import com.chemical.dto.request.UserCreateRequestDTO;
import com.chemical.dto.request.UserUpdateRequestDTO;
import com.chemical.dto.response.UserResponseDTO;
import com.chemical.entity.Role;
import com.chemical.entity.User;
import com.chemical.mapper.UserMapper;
import com.chemical.repositories.RoleRepository;
import com.chemical.repositories.UserRepository;
import com.chemical.services.UserService;
import com.chemical.utils.GetNotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Page<UserResponseDTO> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<User> specification = new SearchSpecification<>(request);
        Page<User> userPage = userRepository.findAll(specification, pageable);
        List<UserResponseDTO> userSearchResponses = userPage.getContent().stream()
                .map(UserMapper::convertToUserResponse).toList();
        return new PageImpl<>(userSearchResponses, pageable, userPage.getTotalElements());
    }

    @Override
    @Transactional
    public User save(UserCreateRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("User's already exist");
        }

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RecordNotFoundException("Không tìm thấy vai trò: " + request.getRoleId()));
        User user = UserMapper.userCreateRequestConvertToUser(request);

        log.info("this is role: " + role);
        user.setRole(role);
        user.setCreated_by("user");
        user.setUpdated_by("user");
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        log.info("save user in service: " + user);

        return userRepository.save(user);
    }

    public UserResponseDTO currentUserDetails() {
        String email;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        final String finalEmail = email;

        User user = userRepository.findByEmail(finalEmail)
                .orElseThrow(() -> new RecordNotFoundException("Not found event with email: " + finalEmail));

        return UserMapper.convertToUserResponse(user);
    }


    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::convertToUserResponse).toList();
    }

    public UserResponseDTO findByEmailAuth(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException("Không tìm thấy người dùng với: " + email));
        UserResponseDTO currentUser = UserMapper.convertToUserResponse(user);
        return currentUser;
    }

    @Override
    public User update(Long userId, UserUpdateRequestDTO updateRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getId().equals(userId)) {
                throw new LogicException("Id không đúng định dạng");
            }
            Role role = roleRepository.findById(updateRequest.getRoleId()).orElseThrow(() -> new RecordNotFoundException("Không tìm thấy vai trò: " + updateRequest.getRoleId()));
            if (role != null) {
                String roleName = role.getName();
                if (roleName != null) {
                    Role foundRole = roleRepository.findByName(roleName).orElse(null);
                    if (foundRole != null) {
                        user.setRole(foundRole);
                    } else {
                        throw new IllegalArgumentException("Tên vai trò không tồn tại: " + roleName);
                    }
                } else {
                    throw new IllegalArgumentException("Vai trò không được rỗng");
                }
            } else {
                throw new IllegalArgumentException("Không tìm thấy vai trò");
            }
            BeanUtils.copyProperties(updateRequest, user, GetNotNull.getNullPropertyNames(updateRequest));
            user.setUpdated_by("user");
            user.setUpdated_at(new Date());
            return userRepository.save(user);
        } else {
            throw new RecordNotFoundException("Người dùng không được tìm thấy");
        }
    }

    @Override
    public void delete(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.debug("Delete Event " + e.getMessage());
            throw new LogicException("Unknown error");
        }
    }
}
