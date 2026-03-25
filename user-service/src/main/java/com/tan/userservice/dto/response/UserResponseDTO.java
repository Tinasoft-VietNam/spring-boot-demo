package com.tan.userservice.dto.response;

import com.tan.userservice.dto.BaseDTO;
import com.tan.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends BaseDTO {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private String gender;
    private Role role;
}
