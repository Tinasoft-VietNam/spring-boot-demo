package com.chemical.dto.response;

import com.chemical.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.chemical.entity.Role;

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
