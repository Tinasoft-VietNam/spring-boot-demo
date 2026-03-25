package com.example.businessservice.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemicalCategory {

    private Long id;
    private Chemical chemical;
    private Category category;

}
