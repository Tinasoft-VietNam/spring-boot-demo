package com.example.businessservice.application.service;


import com.example.businessservice.infrastructure.config.common.query.SearchRequest;
import com.example.businessservice.domain.entity.Chemical;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChemicalService {
    Page<Chemical> search(SearchRequest request);
    List<Chemical> getAllChemicals();

    Chemical findById(Long chemicalId);

    Chemical findDetailsById(Long chemicalId);

    Chemical save(Chemical chemical);

    Chemical update(Long chemicalId, Chemical chemical);

    void delete(Long chemicalId);
}
