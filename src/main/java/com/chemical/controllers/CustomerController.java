package com.chemical.controllers;

import com.chemical.common.BasePaginationResponse;
import com.chemical.common.BaseResponse;
import com.chemical.common.query.SearchRequest;
import com.chemical.dto.request.CustomerCreateRequestDTO;
import com.chemical.dto.request.CustomerUpdateRequestDTO;
import com.chemical.dto.response.CustomerResponseDTO;
import com.chemical.entity.Customer;
import com.chemical.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {
    public  final CustomerService customerService;

    @PostMapping("/search")
    public BasePaginationResponse<List<CustomerResponseDTO>> searchCustomer(@RequestBody SearchRequest request) {
        Page<CustomerResponseDTO> page = customerService.search(request);
        return BasePaginationResponse.ok(page.getContent(), request.getPage(), page.getTotalPages(), (int) page.getTotalElements());
    }
    @GetMapping("/get-all")
    public BaseResponse<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        return BaseResponse.ok(customers);
    }

    @GetMapping("/detail/{id}")
    public BaseResponse<CustomerResponseDTO> getDetailCustomer(@PathVariable("id") Long id) {
        CustomerResponseDTO customer = customerService.findDetailsById(id);
        return BaseResponse.ok(customer);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Customer> createCustomer(@Valid @RequestBody CustomerCreateRequestDTO request) {
        Customer savedCustomer = customerService.save(request);
        return BaseResponse.created(savedCustomer);
    }

    @PutMapping("/update/{id}")
    public BaseResponse<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerUpdateRequestDTO request) {
        log.info("request to update customer with id:  " + id);
        Customer updatedCustomer = customerService.update(id, request);
        return BaseResponse.ok(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteCustomer(@PathVariable("id") Long id) {
        log.info("request to delete customer with id:  " + id);
        customerService.delete(id);
        return BaseResponse.ok(null);
    }
}
