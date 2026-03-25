package com.tan.userservice.dto.response;

import com.tan.userservice.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleResponseDTO extends BaseDTO {
    private Long id;
    private String name;
    private String slug;
    private List<PermissionResponseDTO> permissions;
}
