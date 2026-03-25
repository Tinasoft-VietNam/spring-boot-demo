package com.tan.userservice.services;

import com.tan.userservice.common.query.SearchRequest;
import com.tan.userservice.dto.request.ProfileUpdateRequestDTO;
import com.tan.userservice.dto.request.UserCreateRequestDTO;
import com.tan.userservice.dto.request.UserUpdateRequestDTO;
import com.tan.userservice.dto.response.UserResponseDTO;
import com.tan.userservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UserResponseDTO> search(SearchRequest request);

    User create(UserCreateRequestDTO request);

    User save(UserCreateRequestDTO request);

    UserResponseDTO currentUserDetails();

    UserResponseDTO findById(Long userId);

    UserResponseDTO findByEmailAuth(String email);

    List<UserResponseDTO> getAllUsers();

    User update(Long userId, UserUpdateRequestDTO request);

    UserResponseDTO updateMyProfile(ProfileUpdateRequestDTO request);

    void delete(Long userId);
}
