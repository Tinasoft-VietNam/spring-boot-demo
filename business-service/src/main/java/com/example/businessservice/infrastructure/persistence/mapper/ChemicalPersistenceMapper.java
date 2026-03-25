package com.example.businessservice.infrastructure.persistence.mapper;

import com.example.businessservice.domain.entity.Chemical;
import com.example.businessservice.infrastructure.persistence.entity.ChemicalJpaEntity;
import org.modelmapper.ModelMapper;

public class ChemicalPersistenceMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ChemicalJpaEntity toEntity(Chemical domain) {
        return modelMapper.map(domain, ChemicalJpaEntity.class);
    }

    public static Chemical toDomain(ChemicalJpaEntity entity) {
        return modelMapper.map(entity, Chemical.class);
    }
}
