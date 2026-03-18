package com.chemical.mapper;

import com.chemical.dto.request.ChemicalCreateRequestDTO;
import com.chemical.dto.response.CategoryResponseDTO;
import com.chemical.dto.response.ChemicalResponseDTO;
import com.chemical.dto.response.ManufacturerResponseDTO;
import com.chemical.entity.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ChemicalMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Chemical convertChemicalCreateToChemical(ChemicalCreateRequestDTO createRequest) {
        return modelMapper.map(createRequest, Chemical.class);
    }

    public static ChemicalResponseDTO convertToChemicalResponse(Chemical chemical) {
        ChemicalResponseDTO chemicalResponseDTO = modelMapper.map(chemical, ChemicalResponseDTO.class);

        List<CategoryResponseDTO> categories = chemical.getCategories().stream()
                .map(ChemicalMapper::convertCategoryToCategoryResponseDTO)
                .collect(Collectors.toList());

        List<ManufacturerResponseDTO> manufacturers = chemical.getManufacturers().stream()
                .map(ChemicalMapper::convertManufacturerToManufacturerResponseDTO)
                .collect(Collectors.toList());

        chemicalResponseDTO.setCategories(categories);
        chemicalResponseDTO.setManufacturers(manufacturers);
        return chemicalResponseDTO;
    }

    private static CategoryResponseDTO convertCategoryToCategoryResponseDTO(ChemicalCategory chemicalCategory) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(chemicalCategory.getId());
        categoryResponseDTO.setName(chemicalCategory.getCategory().getName());
        categoryResponseDTO.setDescription(chemicalCategory.getCategory().getDescription());
        return categoryResponseDTO;
    }

    private static ManufacturerResponseDTO convertManufacturerToManufacturerResponseDTO(ChemicalManufacturer chemicalManufacturer) {
        ManufacturerResponseDTO manufacturerResponseDTO = new ManufacturerResponseDTO();
        manufacturerResponseDTO.setId(chemicalManufacturer.getId());
        manufacturerResponseDTO.setName(chemicalManufacturer.getManufacturer().getName());
        manufacturerResponseDTO.setAddress(chemicalManufacturer.getManufacturer().getAddress());
        manufacturerResponseDTO.setContact_information(chemicalManufacturer.getManufacturer().getContact_information());
        return manufacturerResponseDTO;
    }

}
