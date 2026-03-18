package com.chemical.mapper;

import com.chemical.dto.request.CustomerCreateRequestDTO;
import com.chemical.dto.response.CustomerResponseDTO;
import com.chemical.dto.response.OrderResponseDTO;
import com.chemical.entity.Customer;
import com.chemical.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerResponseDTO toCustomerResponseDTO(Customer customer);
    Customer convertCustomerRequestToCustomer(CustomerCreateRequestDTO customerCreateRequestDTO);
}
