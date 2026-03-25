package com.example.businessservice.presentation.controller;

import com.example.businessservice.application.service.CustomerService;
import com.example.businessservice.domain.entity.Customer;
import com.example.businessservice.infrastructure.config.common.BasePaginationResponse;
import com.example.businessservice.infrastructure.config.common.BaseResponse;
import com.example.businessservice.infrastructure.config.common.query.SearchRequest;
import com.example.businessservice.presentation.dto.request.CustomerCreateRequestDTO;
import com.example.businessservice.presentation.dto.request.CustomerUpdateRequestDTO;
import com.example.businessservice.presentation.dto.response.CustomerResponseDTO;
import com.example.businessservice.presentation.mapper.CustomerWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {
    public final CustomerService customerService;

    @PostMapping("/search")
    public BasePaginationResponse<List<CustomerResponseDTO>> searchCustomer(@RequestBody SearchRequest request) {
        Page<Customer> page = customerService.search(request);
        List<CustomerResponseDTO> responses = page.getContent().stream()
                .map(CustomerWebMapper::toResponseDTO)
                .collect(Collectors.toList());
        return BasePaginationResponse.ok(responses, request.getPage(), page.getTotalPages(), (int) page.getTotalElements());
    }

    @GetMapping("/get-all")
    public BaseResponse<List<CustomerResponseDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponseDTO> responses = customers.stream()
                .map(CustomerWebMapper::toResponseDTO)
                .collect(Collectors.toList());
        return BaseResponse.ok(responses);
    }

    @GetMapping("/detail/{id}")
    public BaseResponse<CustomerResponseDTO> getDetailCustomer(@PathVariable("id") Long id) {
        Customer customer = customerService.findDetailsById(id);
        return BaseResponse.ok(CustomerWebMapper.toResponseDTO(customer));
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateRequestDTO request) {
        Customer customerInput = CustomerWebMapper.toDomain(request);
        Customer savedCustomer = customerService.save(customerInput);
        return BaseResponse.created(CustomerWebMapper.toResponseDTO(savedCustomer));
    }

    @PutMapping("/update/{id}")
    public BaseResponse<CustomerResponseDTO> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerUpdateRequestDTO request) {
        log.info("request to update customer with id:  " + id);
        Customer customerInput = new Customer();
        BeanUtils.copyProperties(request, customerInput);
        Customer updatedCustomer = customerService.update(id, customerInput);
        return BaseResponse.ok(CustomerWebMapper.toResponseDTO(updatedCustomer));
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteCustomer(@PathVariable("id") Long id) {
        log.info("request to delete customer with id:  " + id);
        customerService.delete(id);
        return BaseResponse.ok(null);
    }
}
