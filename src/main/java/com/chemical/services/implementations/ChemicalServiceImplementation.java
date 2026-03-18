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
import com.chemical.utils.GetNotNull;
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
    public Page<ChemicalResponseDTO> search(SearchRequest request) {
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        SearchSpecification<Chemical> specification = new SearchSpecification<>(request);
        Page<Chemical> chemicalPage = chemicalRepository.findAll(specification, pageable);
        List<ChemicalResponseDTO> chemicalSearchResponses = chemicalPage.getContent().stream()
                .map(ChemicalMapper::convertToChemicalResponse).toList();
        return new PageImpl<>(chemicalSearchResponses, pageable, chemicalPage.getTotalElements());
    }

    @Override
    public List<ChemicalResponseDTO> getAllChemicals() {
        return chemicalRepository.findAll().stream().map(ChemicalMapper::convertToChemicalResponse).toList();
    }

    @Override
    public Chemical findById(Long chemicalId) {
        return chemicalRepository.findById(chemicalId).orElseThrow(() -> new RecordNotFoundException(" Not found chemical with id : " + chemicalId));
    }
    @Override
    public ChemicalResponseDTO findDetailsById(Long chemicalId) {
        Chemical chemical = findById(chemicalId);
        return ChemicalMapper.convertToChemicalResponse(chemical);
    }

    @Override
    public Chemical save(ChemicalCreateRequestDTO createRequest) {
        Chemical chemical = ChemicalMapper.convertChemicalCreateToChemical(createRequest);

        chemical.setCreated_by("user");
        chemical.setUpdated_by("user");
        chemical.setCreated_at(new Date());
        chemical.setUpdated_at(new Date());
        log.info("save chemical in service: " + chemical);

        return chemicalRepository.save(chemical);
    }

    @Override
    public Chemical update(Long chemicalId, ChemicalUpdateRequestDTO updateRequest) {
        Chemical chemical = findById(chemicalId);
        if (chemical.getId() != chemicalId) {
            throw new LogicException("Id is not match");
        }

        BeanUtils.copyProperties(updateRequest, chemical, GetNotNull.getNullPropertyNames(updateRequest));

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
