package com.example.businessservice.infrastructure.persistence.mapper;

import com.example.businessservice.domain.entity.Customer;
import com.example.businessservice.infrastructure.persistence.entity.CustomerJpaEntity;
import org.modelmapper.ModelMapper;

public class CustomerPersistenceMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CustomerJpaEntity toEntity(Customer domain) {
        return modelMapper.map(domain, CustomerJpaEntity.class);
    }

    public static Customer toDomain(CustomerJpaEntity entity) {
        return modelMapper.map(entity, Customer.class);
    }
}
