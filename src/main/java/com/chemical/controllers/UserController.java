package com.chemical.controllers;

import com.chemical.common.BasePaginationResponse;
import com.chemical.common.BaseResponse;
import com.chemical.common.query.SearchRequest;
import com.chemical.dto.request.UserUpdateRequestDTO;
import com.chemical.dto.response.UserResponseDTO;
import com.chemical.entity.User;
import com.chemical.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Tag(name = "User Controller", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    @Operation(summary = "Search users with pagination")
    @PostMapping("/search")
    public BasePaginationResponse<List<UserResponseDTO>> searchUser(@RequestBody SearchRequest request) {
        Page<UserResponseDTO> page = userService.search(request);
        return BasePaginationResponse.ok(page.getContent(), request.getPage(), page.getTotalPages(), (int) page.getTotalElements());
    }
    @Operation(summary = "Get details of the currently authenticated user")
    @GetMapping("/current-user")
    public BaseResponse<UserResponseDTO> getCurrentUser() throws Exception {
        try {
            UserResponseDTO user = userService.currentUserDetails();
            return BaseResponse.ok(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    @Operation(summary = "Get all users")
    @GetMapping("/get-all")
    public BaseResponse<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return BaseResponse.ok(users);
    }

    @Operation(summary = "Update user details by ID")
    @PutMapping("/update/{id}")
    public BaseResponse<User> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        User user = userService.update(id, request);
        return BaseResponse.ok(user);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteUser(@PathVariable("id") Long id) {
        log.info("request to update Section with id:  " + id);
        userService.delete(id);
        return BaseResponse.ok(null);
    }
}
