package com.chemical.services.implementations;

import com.chemical.common.errors.LogicException;
import com.chemical.common.errors.RecordNotFoundException;
import com.chemical.common.query.SearchRequest;
import com.chemical.common.query.SearchSpecification;
import com.chemical.dto.request.ChemicalCreateRequestDTO;
import com.chemical.dto.request.ChemicalUpdateRequestDTO;
import com.chemical.dto.response.ChemicalResponseDTO;
import com.chemical.entity.Chemical;
import com.chemical.mapper.ChemicalMapper;
import com.chemical.repositories.ChemicalRepository;
import com.chemical.services.ChemicalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
public class ChemicalServiceImplementation implements ChemicalService {

    ChemicalRepository chemicalRepository;
    ChemicalMapper chemicalMapper;

    @Override
    public Page<ChemicalResponseDTO> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<Chemical> specification = new SearchSpecification<>(request);
        Page<Chemical> chemicalPage = chemicalRepository.findAll(specification, pageable);
        List<ChemicalResponseDTO> chemicalSearchResponses = chemicalMapper.toDtoList(chemicalPage.getContent());
        return new PageImpl<>(chemicalSearchResponses, pageable, chemicalPage.getTotalElements());
    }

    @Override
    public List<ChemicalResponseDTO> getAllChemicals() {
        return chemicalMapper.toDtoList(chemicalRepository.findAll());
    }

    @Override
    public Chemical findById(Long chemicalId) {
        return chemicalRepository.findById(chemicalId).orElseThrow(() -> new RecordNotFoundException(" Not found chemical with id : " + chemicalId));
    }

    @Override
    public ChemicalResponseDTO findDetailsById(Long chemicalId) {
        Chemical chemical = findById(chemicalId);
        return chemicalMapper.toDto(chemical);
    }

    @Override
    public Chemical save(ChemicalCreateRequestDTO createRequest) {
        Chemical chemical = chemicalMapper.toEntity(createRequest);

        chemical.setCreated_by("user");
        chemical.setUpdated_by("user");
        chemical.setCreated_at(new Date());
        chemical.setUpdated_at(new Date());

        return chemicalRepository.save(chemical);
    }

    @Override
    public Chemical update(Long chemicalId, ChemicalUpdateRequestDTO updateRequest) {
        Chemical chemical = findById(chemicalId);

        chemicalMapper.updateEntity(updateRequest, chemical);

        chemical.setUpdated_at(new Date());
        chemical.setUpdated_by("user");

        return chemicalRepository.save(chemical);
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
