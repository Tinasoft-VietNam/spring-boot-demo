package com.chemical.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO {
    private String name;
    private String avatar;
    private Integer gender;
    private String email;
    private String password;
    private Long roleId;
}
