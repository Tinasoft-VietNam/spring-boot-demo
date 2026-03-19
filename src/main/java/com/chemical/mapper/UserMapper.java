package com.chemical.mapper;

import com.chemical.dto.request.UserCreateRequestDTO;
import com.chemical.dto.request.UserUpdateRequestDTO;
import com.chemical.dto.response.UserResponseDTO;
import com.chemical.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO convertToUserResponse(User user);

    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User userCreateRequestConvertToUser(UserCreateRequestDTO request);

    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void userUpdateRequestConvertToUser(@MappingTarget User user, UserUpdateRequestDTO request);
}
