package com.chemical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDTO {
    private Long id;
    private String table_key;
    private Integer is_read;
    private Integer is_create;
    private Integer is_update;
    private Integer is_delete;
    private Integer is_manage;
}
