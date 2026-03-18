package com.chemical.controllers;

import com.chemical.common.BasePaginationResponse;
import com.chemical.common.BaseResponse;
import com.chemical.common.query.SearchRequest;
import com.chemical.dto.request.UserUpdateRequestDTO;
import com.chemical.dto.response.UserResponseDTO;
import com.chemical.entity.User;
import com.chemical.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public BasePaginationResponse<List<UserResponseDTO>> searchUser(@RequestBody SearchRequest request) {
        Page<UserResponseDTO> page = userService.search(request);
        return BasePaginationResponse.ok(page.getContent(), request.getPage(), page.getTotalPages(), (int) page.getTotalElements());
    }
    @GetMapping("/current-user")
    public BaseResponse<UserResponseDTO> getCurrentUser() throws Exception {
        try {
            UserResponseDTO user = userService.currentUserDetails();
            return BaseResponse.ok(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    @GetMapping("/get-all")
    public BaseResponse<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return BaseResponse.ok(users);
    }

    @PutMapping("/update/{id}")
    public BaseResponse<User> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequestDTO request) {
        log.info("request to update Section with id:  " + id);
        User user = userService.update(id, request);
        return BaseResponse.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteUser(@PathVariable("id") Long id) {
        log.info("request to update Section with id:  " + id);
        userService.delete(id);
        return BaseResponse.ok(null);
    }
}
