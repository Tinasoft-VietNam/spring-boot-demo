package com.example.businessservice.application.service.implementations;

import com.example.businessservice.application.repository.ChemicalRepository;
import com.example.businessservice.application.service.ChemicalService;
import com.example.businessservice.domain.entity.Chemical;
import com.example.businessservice.infrastructure.config.common.errors.LogicException;
import com.example.businessservice.infrastructure.config.common.errors.RecordNotFoundException;
import com.example.businessservice.infrastructure.config.common.query.SearchRequest;
import com.example.businessservice.infrastructure.config.common.query.SearchSpecification;
import com.example.businessservice.infrastructure.persistence.entity.ChemicalJpaEntity;
import com.example.businessservice.infrastructure.persistence.mapper.ChemicalPersistenceMapper;
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
public class ChemicalServiceImplementation implements ChemicalService {

    private final ChemicalRepository chemicalRepository;

    @Override
    public Page<Chemical> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<ChemicalJpaEntity> specification = new SearchSpecification<>(request);
        Page<ChemicalJpaEntity> chemicalPage = chemicalRepository.findAll(specification, pageable);
        List<Chemical> chemicals = chemicalPage.getContent().stream()
                .map(ChemicalPersistenceMapper::toDomain).toList();
        return new PageImpl<>(chemicals, pageable, chemicalPage.getTotalElements());
    }

    @Override
    public List<Chemical> getAllChemicals() {
        return chemicalRepository.findAll().stream().map(ChemicalPersistenceMapper::toDomain).toList();
    }

    @Override
    public Chemical findById(Long chemicalId) {
        ChemicalJpaEntity entity = chemicalRepository.findById(chemicalId).orElseThrow(() -> new RecordNotFoundException(" Not found chemical with id : " + chemicalId));
        return ChemicalPersistenceMapper.toDomain(entity);
    }

    @Override
    public Chemical findDetailsById(Long chemicalId) {
        return findById(chemicalId);
    }

    @Override
    public Chemical save(Chemical chemical) {
        ChemicalJpaEntity entity = ChemicalPersistenceMapper.toEntity(chemical);

        entity.setCreated_by("user");
        entity.setUpdated_by("user");
        entity.setCreated_at(new Date());
        entity.setUpdated_at(new Date());
        log.info("save chemical in service: " + entity);

        return ChemicalPersistenceMapper.toDomain(chemicalRepository.save(entity));
    }

    @Override
    public Chemical update(Long chemicalId, Chemical chemical) {
        ChemicalJpaEntity entity = chemicalRepository.findById(chemicalId)
                .orElseThrow(() -> new RecordNotFoundException(" Not found chemical with id : " + chemicalId));
        if (entity.getId() != chemicalId) {
            throw new LogicException("Id is not match");
        }

        BeanUtils.copyProperties(chemical, entity, GetNotNull.getNullPropertyNames(chemical));

        entity.setUpdated_at(new Date());
        entity.setUpdated_by("user");
        return ChemicalPersistenceMapper.toDomain(chemicalRepository.save(entity));
    }

    @Override
    public void delete(Long chemicalId) {
        try {
            chemicalRepository.deleteById(chemicalId);
        } catch (Exception e) {
            log.debug("Delete chemical " + e.getMessage());
            throw new LogicException("Unknown error");
        }
    }
}
