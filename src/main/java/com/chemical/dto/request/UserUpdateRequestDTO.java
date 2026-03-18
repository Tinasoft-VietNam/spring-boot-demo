package com.chemical.dto.request;

import com.chemical.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Long roleId;
}
