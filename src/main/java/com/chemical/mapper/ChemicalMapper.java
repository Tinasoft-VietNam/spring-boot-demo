package com.chemical.mapper;

import com.chemical.dto.request.ChemicalCreateRequestDTO;
import com.chemical.dto.request.ChemicalUpdateRequestDTO;
import com.chemical.dto.response.ChemicalResponseDTO;
import com.chemical.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ManufacturerMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChemicalMapper {

    Chemical toEntity(ChemicalCreateRequestDTO request);

    ChemicalResponseDTO toDto(Chemical chemical);

    List<ChemicalResponseDTO> toDtoList(List<Chemical> chemicals);

    void updateEntity(ChemicalUpdateRequestDTO request, @MappingTarget Chemical chemical);
}
