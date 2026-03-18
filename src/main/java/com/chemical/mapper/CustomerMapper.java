package com.chemical.mapper;

import com.chemical.dto.request.CustomerCreateRequestDTO;
import com.chemical.dto.response.CustomerResponseDTO;
import com.chemical.dto.response.OrderResponseDTO;
import com.chemical.entity.Customer;
import com.chemical.entity.Order;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CustomerResponseDTO convertToCustomerResponse(Customer customer) {
        CustomerResponseDTO customerResponseDTO = modelMapper.map(customer, CustomerResponseDTO.class);

        List<OrderResponseDTO> orders = customer.getOrders().stream()
                .map(CustomerMapper::convertOrderToCustomerResponseDTO)
                .collect(Collectors.toList());

        customerResponseDTO.setOrders(orders);
        return customerResponseDTO;
    }

    public static Customer convertCustomerCreateToCustomer(CustomerCreateRequestDTO createRequest) {
        return modelMapper.map(createRequest, Customer.class);
    }
    private static OrderResponseDTO convertOrderToCustomerResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setTransport_id(order.getTransport_id());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setTotal(order.getTotal());
        orderResponseDTO.setNote(order.getNote());
        orderResponseDTO.setOrder_time(order.getOrder_time());
        return orderResponseDTO;
    }
}
