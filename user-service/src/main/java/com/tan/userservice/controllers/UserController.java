package com.tan.userservice.controllers;

import com.tan.userservice.common.BasePaginationResponse;
import com.tan.userservice.common.BaseResponse;
import com.tan.userservice.common.query.SearchRequest;
import com.tan.userservice.dto.request.UserCreateRequestDTO;
import com.tan.userservice.dto.request.UserUpdateRequestDTO;
import com.tan.userservice.dto.response.UserResponseDTO;
import com.tan.userservice.entity.User;
import com.tan.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'USER_READ')")
    public BasePaginationResponse<List<UserResponseDTO>> searchUser(@RequestBody SearchRequest request) {
        Page<UserResponseDTO> page = userService.search(request);
        return BasePaginationResponse.ok(page.getContent(), request.getPage(), page.getTotalPages(),
                (int) page.getTotalElements());
    }

    @GetMapping("/current-user")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<UserResponseDTO> getCurrentUser() throws Exception {
        try {
            UserResponseDTO user = userService.currentUserDetails();
            return BaseResponse.ok(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'USER_READ')")
    public BaseResponse<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return BaseResponse.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'USER_READ')")
    public BaseResponse<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        return BaseResponse.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'USER_CREATE')")
    public BaseResponse<User> createUser(@Valid @RequestBody UserCreateRequestDTO request) {
        User savedUser = userService.create(request);
        return BaseResponse.created(savedUser);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'USER_UPDATE')")
    public BaseResponse<User> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        User user = userService.update(id, request);
        return BaseResponse.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'USER_DELETE')")
    public BaseResponse<Void> deleteUser(@PathVariable("id") Long id) {
        log.info("request to update Section with id:  " + id);
        userService.delete(id);
        return BaseResponse.ok(null);
    }
}
