package com.example.businessservice.presentation.controller;

import com.example.businessservice.application.service.ChemicalService;
import com.example.businessservice.domain.entity.Chemical;
import com.example.businessservice.infrastructure.config.common.BasePaginationResponse;
import com.example.businessservice.infrastructure.config.common.BaseResponse;
import com.example.businessservice.infrastructure.config.common.query.SearchRequest;
import com.example.businessservice.presentation.dto.request.ChemicalCreateRequestDTO;
import com.example.businessservice.presentation.dto.request.ChemicalUpdateRequestDTO;
import com.example.businessservice.presentation.dto.response.ChemicalResponseDTO;
import com.example.businessservice.presentation.mapper.ChemicalWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Chemical Controller", description = "Endpoints for managing chemicals")
@RestController
@RequestMapping("/api/chemical")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChemicalController {
    public final ChemicalService chemicalService;

    @Operation(summary = "Search chemicals with pagination")
    @PostMapping("/search")
    public BasePaginationResponse<List<ChemicalResponseDTO>> searchChemical(@RequestBody SearchRequest request) {
        Page<Chemical> page = chemicalService.search(request);
        List<ChemicalResponseDTO> responses = page.getContent().stream()
                .map(ChemicalWebMapper::toResponseDTO)
                .collect(Collectors.toList());
        return BasePaginationResponse.ok(responses, request.getPage(), page.getTotalPages(), (int) page.getTotalElements());
    }

    @Operation(summary = "Get all chemicals")
    @GetMapping("/get-all")
    public BaseResponse<List<ChemicalResponseDTO>> getAllChemicals() {
        List<Chemical> chemicals = chemicalService.getAllChemicals();
        List<ChemicalResponseDTO> responses = chemicals.stream()
                .map(ChemicalWebMapper::toResponseDTO)
                .collect(Collectors.toList());
        return BaseResponse.ok(responses);
    }

    @Operation(summary = "Get chemical details by ID")
    @GetMapping("/detail/{id}")
    public BaseResponse<ChemicalResponseDTO> getDetailChemical(@PathVariable("id") Long id) {
        Chemical chemical = chemicalService.findDetailsById(id);
        return BaseResponse.ok(ChemicalWebMapper.toResponseDTO(chemical));
    }

    @Operation(summary = "Create a new chemical")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<ChemicalResponseDTO> createChemical(@Valid @RequestBody ChemicalCreateRequestDTO request) {
        Chemical chemicalInput = ChemicalWebMapper.toDomain(request);
        Chemical savedChemical = chemicalService.save(chemicalInput);
        return BaseResponse.created(ChemicalWebMapper.toResponseDTO(savedChemical));
    }

    @Operation(summary = "Update chemical details by ID")
    @PutMapping("/update/{id}")
    public BaseResponse<ChemicalResponseDTO> updateChemical(@PathVariable("id") Long id, @RequestBody ChemicalUpdateRequestDTO request) {
        log.info("request to update chemical with id:  " + id);
        Chemical chemicalInput = new Chemical();
        BeanUtils.copyProperties(request, chemicalInput);
        Chemical updatedChemical = chemicalService.update(id, chemicalInput);
        return BaseResponse.ok(ChemicalWebMapper.toResponseDTO(updatedChemical));
    }

    @Operation(summary = "Delete chemical by ID")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteChemical(@PathVariable("id") Long id) {
        log.info("request to delete chemical with id:  " + id);
        chemicalService.delete(id);
        return BaseResponse.ok(null);
    }
}
