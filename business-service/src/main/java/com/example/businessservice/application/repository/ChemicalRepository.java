package com.example.businessservice.application.repository;

import com.example.businessservice.infrastructure.persistence.entity.ChemicalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChemicalRepository extends JpaRepository<ChemicalJpaEntity, Long>, JpaSpecificationExecutor<ChemicalJpaEntity> {
}
