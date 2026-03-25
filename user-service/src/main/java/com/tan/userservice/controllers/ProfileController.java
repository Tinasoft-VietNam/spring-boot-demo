package com.tan.userservice.controllers;

import com.tan.userservice.common.BaseResponse;
import com.tan.userservice.dto.request.ProfileUpdateRequestDTO;
import com.tan.userservice.dto.response.UserResponseDTO;
import com.tan.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<UserResponseDTO> getMyProfile() {
        return BaseResponse.ok(userService.currentUserDetails());
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<UserResponseDTO> updateMyProfile(@Valid @RequestBody ProfileUpdateRequestDTO request) {
        return BaseResponse.ok(userService.updateMyProfile(request));
    }
}
