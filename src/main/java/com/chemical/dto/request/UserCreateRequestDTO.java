package com.chemical.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO {
    @NonNull
    private String name;
    private String avatar;
    private Integer gender;
    @NonNull
    @Email
    private String email;
    @NonNull
    @Size(min = 2)
    private String password;

    @NonNull
    private Long roleId;
}
