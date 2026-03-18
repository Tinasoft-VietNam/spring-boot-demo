package com.chemical.mapper;

import com.chemical.dto.request.UserCreateRequestDTO;
import com.chemical.dto.request.UserUpdateRequestDTO;
import com.chemical.dto.response.UserResponseDTO;
import com.chemical.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.modelmapper.ModelMapper;
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO convertToUserResponse(User user);

    @Mapping(target = "role",ignore = true)
    User userCreateRequestConvertToUser(UserCreateRequestDTO request);

    void userUpdateRequestConvertToUser(@MappingTarget User user, UserUpdateRequestDTO request);
}
