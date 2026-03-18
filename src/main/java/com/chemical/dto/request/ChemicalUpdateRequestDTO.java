package com.chemical.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChemicalUpdateRequestDTO {
    private Long id;
    private String name;
    private String description;
    private String cas_number;
    private String file_path;
    private Double flash_point;
    private Double storage_temperature_min;
    private Double storage_temperature_max;
    private String storage_conditions;
    private String type;
    private String unit_of_measure;
}
