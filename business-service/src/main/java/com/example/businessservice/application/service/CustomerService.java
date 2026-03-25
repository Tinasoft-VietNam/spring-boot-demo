package com.example.businessservice.application.service;


import com.example.businessservice.infrastructure.config.common.query.SearchRequest;
import com.example.businessservice.domain.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    Page<Customer> search(SearchRequest request);
    List<Customer> getAllCustomers();

    Customer findById(Long customerId);

    Customer findDetailsById(Long customerId);

    Customer save(Customer customer);

    Customer update(Long customerId, Customer customer);

    void delete(Long customerId);


}
