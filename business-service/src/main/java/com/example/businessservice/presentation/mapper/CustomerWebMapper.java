package com.example.businessservice.presentation.mapper;

import com.example.businessservice.domain.entity.Customer;
import com.example.businessservice.domain.entity.Order;
import com.example.businessservice.presentation.dto.request.CustomerCreateRequestDTO;
import com.example.businessservice.presentation.dto.response.CustomerResponseDTO;
import com.example.businessservice.presentation.dto.response.OrderResponseDTO;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

public class CustomerWebMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Customer toDomain(CustomerCreateRequestDTO requestDTO) {
        return modelMapper.map(requestDTO, Customer.class);
    }

    public static CustomerResponseDTO toResponseDTO(Customer customer) {
        CustomerResponseDTO responseDTO = modelMapper.map(customer, CustomerResponseDTO.class);
        
        if (customer.getOrders() != null) {
            responseDTO.setOrders(customer.getOrders().stream()
                .map(CustomerWebMapper::toOrderResponseDTO)
                .collect(Collectors.toList()));
        }
        return responseDTO;
    }

    private static OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTransport_id(order.getTransport_id());
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());
        dto.setNote(order.getNote());
        dto.setOrder_time(order.getOrder_time());
        return dto;
    }
}
