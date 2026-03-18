package com.chemical.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="chemicals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Chemical extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false, unique = true)
    private String cas_number;

    @Column(nullable = false, unique = true)
    private String file_path;

    @Column
    private Double flash_point;

    @Column
    private Double storage_temperature_min;

    @Column
    private Double storage_temperature_max;

    @Column
    private String storage_conditions;

    @Column
    private String type;

    @Column(nullable = false)
    private String unit_of_measure;

    @OneToMany(mappedBy = "chemical")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JsonIgnoreProperties(value = {"chemical"})
    private List<ChemicalCategory> categories;

    @OneToMany(mappedBy = "chemical")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JsonIgnoreProperties(value = {"chemical"})
    private List<ChemicalManufacturer> manufacturers;

}
