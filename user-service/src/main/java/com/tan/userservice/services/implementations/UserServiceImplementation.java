package com.tan.userservice.services.implementations;

import com.tan.userservice.common.errors.ConflictException;
import com.tan.userservice.common.errors.LogicException;
import com.tan.userservice.common.errors.RecordNotFoundException;
import com.tan.userservice.common.query.SearchRequest;
import com.tan.userservice.common.query.SearchSpecification;
import com.tan.userservice.dto.request.ProfileUpdateRequestDTO;
import com.tan.userservice.dto.request.UserCreateRequestDTO;
import com.tan.userservice.dto.request.UserUpdateRequestDTO;
import com.tan.userservice.dto.response.UserResponseDTO;
import com.tan.userservice.entity.Role;
import com.tan.userservice.entity.User;
import com.tan.userservice.mapper.UserMapper;
import com.tan.userservice.repositories.RoleRepository;
import com.tan.userservice.repositories.UserRepository;
import com.tan.userservice.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImplementation implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponseDTO> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<User> specification = new SearchSpecification<>(request);
        Page<User> userPage = userRepository.findAll(specification, pageable);
        List<UserResponseDTO> userSearchResponses = userPage.getContent().stream()
                .map(userMapper::convertToUserResponse).toList();
        return new PageImpl<>(userSearchResponses, pageable, userPage.getTotalElements());
    }

    @Override
    public User create(UserCreateRequestDTO request) {
        return save(request);
    }

    @Override
    @Transactional
    public User save(UserCreateRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("User's already exist");
        }
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy vai trò: " + request.getRoleId()));
        User user = userMapper.userCreateRequestConvertToUser(request);

        log.info("this is role: " + role);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated_by(getCurrentEmail());
        user.setUpdated_by(getCurrentEmail());
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        log.info("save user in service: " + user);

        return userRepository.save(user);
    }

    public UserResponseDTO currentUserDetails() {
        String email = getCurrentEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("Not found user with email: " + email));

        return userMapper.convertToUserResponse(user);
    }

    @Override
    public UserResponseDTO findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy người dùng id = " + userId));
        return userMapper.convertToUserResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::convertToUserResponse).toList();
    }

    public UserResponseDTO findByEmailAuth(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy người dùng với: " + email));
        return userMapper.convertToUserResponse(user);
    }

    @Override
    public User update(Long userId, UserUpdateRequestDTO updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy người dùng id = " + userId));

        userMapper.userUpdateRequestConvertToUser(user, updateRequest);

        if (updateRequest.getRoleId() != null) {
            Role role = roleRepository.findById(updateRequest.getRoleId())
                    .orElseThrow(
                            () -> new RecordNotFoundException("Không tìm thấy vai trò: " + updateRequest.getRoleId()));
            user.setRole(role);
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        user.setUpdated_by(getCurrentEmail());
        user.setUpdated_at(new Date());

        return userRepository.save(user);

    }

    @Override
    public UserResponseDTO updateMyProfile(ProfileUpdateRequestDTO request) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy người dùng với: " + email));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setUpdated_at(new Date());
        user.setUpdated_by(user.getEmail());

        return userMapper.convertToUserResponse(userRepository.save(user));
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

    private String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RecordNotFoundException("Authentication is missing");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return authentication.getName();
    }
}
