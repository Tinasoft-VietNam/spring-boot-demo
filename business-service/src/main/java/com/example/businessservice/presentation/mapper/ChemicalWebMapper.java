package com.example.businessservice.presentation.mapper;

import com.example.businessservice.domain.entity.Chemical;
import com.example.businessservice.domain.entity.ChemicalCategory;
import com.example.businessservice.domain.entity.ChemicalManufacturer;
import com.example.businessservice.presentation.dto.request.ChemicalCreateRequestDTO;
import com.example.businessservice.presentation.dto.response.CategoryResponseDTO;
import com.example.businessservice.presentation.dto.response.ChemicalResponseDTO;
import com.example.businessservice.presentation.dto.response.ManufacturerResponseDTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ChemicalWebMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Chemical toDomain(ChemicalCreateRequestDTO requestDTO) {
        return modelMapper.map(requestDTO, Chemical.class);
    }

    public static ChemicalResponseDTO toResponseDTO(Chemical chemical) {
        ChemicalResponseDTO responseDTO = modelMapper.map(chemical, ChemicalResponseDTO.class);
        
        if (chemical.getCategories() != null) {
            responseDTO.setCategories(chemical.getCategories().stream()
                .map(ChemicalWebMapper::toCategoryResponseDTO)
                .collect(Collectors.toList()));
        }
        
        if (chemical.getManufacturers() != null) {
            responseDTO.setManufacturers(chemical.getManufacturers().stream()
                .map(ChemicalWebMapper::toManufacturerResponseDTO)
                .collect(Collectors.toList()));
        }
        return responseDTO;
    }

    private static CategoryResponseDTO toCategoryResponseDTO(ChemicalCategory chemicalCategory) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(chemicalCategory.getId());
        dto.setName(chemicalCategory.getCategory().getName());
        dto.setDescription(chemicalCategory.getCategory().getDescription());
        return dto;
    }

    private static ManufacturerResponseDTO toManufacturerResponseDTO(ChemicalManufacturer chemicalManufacturer) {
        ManufacturerResponseDTO dto = new ManufacturerResponseDTO();
        dto.setId(chemicalManufacturer.getId());
        dto.setName(chemicalManufacturer.getManufacturer().getName());
        dto.setAddress(chemicalManufacturer.getManufacturer().getAddress());
        dto.setContact_information(chemicalManufacturer.getManufacturer().getContact_information());
        return dto;
    }
}
