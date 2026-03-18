package com.chemical.dto.response;

import com.chemical.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChemicalResponseDTO extends BaseDTO {
    private Long id;
    private String name;
    private String description;
    private String cas_number;
    private String file_path;
    private Double flash_point;
    private Double storage_temperature_range;
    private String storage_conditions;
    private String type;
    private String unit_of_measure;
    private List<CategoryResponseDTO> categories;
    private List<ManufacturerResponseDTO> manufacturers;
}
