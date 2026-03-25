package com.example.businessservice.application.repository;

import com.example.businessservice.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerJpaEntity, Long>, JpaSpecificationExecutor<CustomerJpaEntity> {
}
