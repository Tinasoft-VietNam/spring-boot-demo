package com.chemical.mapper;

import com.chemical.dto.request.ChemicalCreateRequestDTO;
import com.chemical.dto.request.ChemicalUpdateRequestDTO;
import com.chemical.dto.response.ChemicalResponseDTO;
import com.chemical.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ManufacturerMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChemicalMapper {

    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "manufacturers", ignore = true)
    Chemical toEntity(ChemicalCreateRequestDTO request);

    @Mapping(target = "storage_temperature_range", source = "storage_temperature_min")
    ChemicalResponseDTO toDto(Chemical chemical);

    List<ChemicalResponseDTO> toDtoList(List<Chemical> chemicals);

    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "manufacturers", ignore = true)
    void updateEntity(ChemicalUpdateRequestDTO request, @MappingTarget Chemical chemical);
}
