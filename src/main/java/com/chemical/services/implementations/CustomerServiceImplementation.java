package com.chemical.services.implementations;

import com.chemical.common.errors.LogicException;
import com.chemical.common.errors.RecordNotFoundException;
import com.chemical.common.query.SearchRequest;
import com.chemical.common.query.SearchSpecification;
import com.chemical.dto.request.CustomerCreateRequestDTO;
import com.chemical.dto.request.CustomerUpdateRequestDTO;
import com.chemical.entity.Customer;
import com.chemical.services.CustomerService;
import com.chemical.dto.response.CustomerResponseDTO;
import com.chemical.mapper.CustomerMapper;
import com.chemical.repositories.CustomerRepository;
import com.chemical.utils.GetNotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImplementation implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @Override
    public Page<CustomerResponseDTO> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<Customer> specification = new SearchSpecification<>(request);
        Page<Customer> customerPage = customerRepository.findAll(specification, pageable);
        List<CustomerResponseDTO> customerSearchResponses = customerPage.getContent().stream()
                .map(customerMapper::toCustomerResponseDTO).toList();
        return new PageImpl<>(customerSearchResponses, pageable, customerPage.getTotalElements());
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toCustomerResponseDTO).toList();
    }

    @Override
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException(" Not found customer with id : " + customerId));
    }
    @Override
    public CustomerResponseDTO findDetailsById(Long customerId) {
        Customer customer = findById(customerId);
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public Customer save(CustomerCreateRequestDTO createRequest) {
        Customer customer = customerMapper.convertCustomerRequestToCustomer(createRequest);

        customer.setCreated_by("user");
        customer.setUpdated_by("user");
        customer.setCreated_at(new Date());
        customer.setUpdated_at(new Date());
        log.info("save customer in service: " + customer);

        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long customerId, CustomerUpdateRequestDTO updateRequest) {
        Customer customer = findById(customerId);
        if (customer.getId() != customerId) {
            throw new LogicException("Id is not match");
        }

        BeanUtils.copyProperties(updateRequest, customer, GetNotNull.getNullPropertyNames(updateRequest));

        customer.setUpdated_at(new Date());
        customer.setUpdated_by("user");
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Long customerId) {
        try {
            customerRepository.deleteById(customerId);
        } catch (Exception e) {
            log.debug("Delete customer " + e.getMessage());
            throw new LogicException("Unknown error");
        }
    }
}
