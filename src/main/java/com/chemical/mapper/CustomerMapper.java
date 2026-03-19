package com.chemical.mapper;

import com.chemical.dto.request.CustomerCreateRequestDTO;
import com.chemical.dto.response.CustomerResponseDTO;
import com.chemical.dto.response.OrderResponseDTO;
import com.chemical.entity.Customer;
import com.chemical.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerResponseDTO toCustomerResponseDTO(Customer customer);

    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer convertCustomerRequestToCustomer(CustomerCreateRequestDTO customerCreateRequestDTO);

    @Mapping(source = "customer.id", target = "customer_id")
    OrderResponseDTO toOrderResponseDTO(Order order);
}
