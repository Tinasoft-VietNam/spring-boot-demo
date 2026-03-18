package com.chemical.mapper;

import com.chemical.dto.response.CategoryResponseDTO;
import com.chemical.entity.ChemicalCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    @Mapping(source = "category.id", target = "id")
    @Mapping(source = "category.name", target = "name")
    @Mapping(source = "category.description", target = "description")
    CategoryResponseDTO toCategoryResponseDTO(ChemicalCategory chemicalCategory);

    List<CategoryResponseDTO> toChemicalCategoryList(List<ChemicalCategory> list);
}
