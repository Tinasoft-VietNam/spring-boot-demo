package com.example.businessservice.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemicalManufacturer{
    private Long id;
    private Chemical chemical;
    private Manufacturer manufacturer;

}
