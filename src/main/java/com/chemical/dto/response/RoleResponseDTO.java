package com.chemical.dto.response;

import com.chemical.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO extends BaseDTO {
    private Long id;
    private String name;
    private String slug;
    private List<PermissionResponseDTO> permissions;
}
