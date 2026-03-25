package com.example.businessservice.application.service.implementations;

import com.example.businessservice.application.repository.CustomerRepository;
import com.example.businessservice.application.service.CustomerService;
import com.example.businessservice.domain.entity.Customer;
import com.example.businessservice.infrastructure.config.common.errors.LogicException;
import com.example.businessservice.infrastructure.config.common.errors.RecordNotFoundException;
import com.example.businessservice.infrastructure.config.common.query.SearchRequest;
import com.example.businessservice.infrastructure.config.common.query.SearchSpecification;
import com.example.businessservice.infrastructure.persistence.entity.CustomerJpaEntity;
import com.example.businessservice.infrastructure.persistence.mapper.CustomerPersistenceMapper;
import com.example.businessservice.utils.GetNotNull;
import lombok.RequiredArgsConstructor;
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
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Page<Customer> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<CustomerJpaEntity> specification = new SearchSpecification<>(request);
        Page<CustomerJpaEntity> customerPage = customerRepository.findAll(specification, pageable);
        List<Customer> customers = customerPage.getContent().stream()
                .map(CustomerPersistenceMapper::toDomain).toList();
        return new PageImpl<>(customers, pageable, customerPage.getTotalElements());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerPersistenceMapper::toDomain).toList();
    }

    @Override
    public Customer findById(Long customerId) {
        CustomerJpaEntity entity = customerRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException(" Not found customer with id : " + customerId));
        return CustomerPersistenceMapper.toDomain(entity);
    }

    @Override
    public Customer findDetailsById(Long customerId) {
        return findById(customerId);
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity entity = CustomerPersistenceMapper.toEntity(customer);

        entity.setCreated_by("user");
        entity.setUpdated_by("user");
        entity.setCreated_at(new Date());
        entity.setUpdated_at(new Date());
        log.info("save customer in service: " + entity);

        return CustomerPersistenceMapper.toDomain(customerRepository.save(entity));
    }

    @Override
    public Customer update(Long customerId, Customer customer) {
        CustomerJpaEntity entity = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException(" Not found customer with id : " + customerId));
        if (entity.getId() != customerId) {
            throw new LogicException("Id is not match");
        }

        BeanUtils.copyProperties(customer, entity, GetNotNull.getNullPropertyNames(customer));

        entity.setUpdated_at(new Date());
        entity.setUpdated_by("user");
        return CustomerPersistenceMapper.toDomain(customerRepository.save(entity));
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
