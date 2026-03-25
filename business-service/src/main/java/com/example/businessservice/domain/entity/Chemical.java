package com.example.businessservice.domain.entity;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chemical {

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
    
    private List<ChemicalCategory> categories;
    private List<ChemicalManufacturer> manufacturers;

}
