package com.example.businessservice.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "chemical_manufacturer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChemicalManufacturerJpaEntity extends BaseJpaEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chemical_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ChemicalJpaEntity chemical;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ManufacturerJpaEntity manufacturer;

}
