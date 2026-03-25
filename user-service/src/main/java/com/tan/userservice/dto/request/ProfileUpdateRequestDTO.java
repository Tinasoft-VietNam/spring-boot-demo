package com.tan.userservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequestDTO {
    private String name;
    private String avatar;
    private Integer gender;

    @Size(min = 2)
    private String password;
}
