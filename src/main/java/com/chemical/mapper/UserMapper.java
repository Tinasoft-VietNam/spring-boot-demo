package com.chemical.mapper;

import com.chemical.dto.request.UserCreateRequestDTO;
import com.chemical.dto.response.UserResponseDTO;
import com.chemical.entity.User;
import org.modelmapper.ModelMapper;

public class UserMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    public static UserResponseDTO convertToUserResponse(User user) {
        UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
        userResponseDTO.setRole(user.getRole());
        return userResponseDTO;
    }
    public static User userCreateRequestConvertToUser(UserCreateRequestDTO request){
        User response = new User();
        response.setName(request.getName());
        response.setAvatar(request.getAvatar());
        response.setGender(request.getGender());
        response.setEmail(request.getEmail());
        response.setPassword(request.getPassword());
        return response;
    }
}
