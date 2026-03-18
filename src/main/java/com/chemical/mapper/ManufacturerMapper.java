package com.chemical.mapper;

import com.chemical.dto.response.ManufacturerResponseDTO;
import com.chemical.entity.ChemicalManufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ManufacturerMapper {

    @Mapping(source = "manufacturer.id", target = "id")
    @Mapping(source = "manufacturer.name", target = "name")
    @Mapping(source = "manufacturer.address", target = "address")
    @Mapping(source = "manufacturer.contact_information", target = "contact_information")
    ManufacturerResponseDTO toManufacturerResponseDTO(ChemicalManufacturer chemicalManufacturer);

    List<ManufacturerResponseDTO> toManufacturerResponseDTOList(List<ChemicalManufacturer> list);
}