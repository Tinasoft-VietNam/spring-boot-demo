package com.chemical.controllers;

import com.chemical.common.BasePaginationResponse;
import com.chemical.common.BaseResponse;
import com.chemical.common.query.SearchRequest;
import com.chemical.dto.request.ChemicalCreateRequestDTO;
import com.chemical.dto.request.ChemicalUpdateRequestDTO;
import com.chemical.dto.response.ChemicalResponseDTO;
import com.chemical.entity.Chemical;
import com.chemical.services.ChemicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Page<ChemicalResponseDTO> page = chemicalService.search(request);
        return BasePaginationResponse.ok(page.getContent(), request.getPage(), page.getTotalPages(),
                (int) page.getTotalElements());
    }

    @Operation(summary = "Get all chemicals")
    @GetMapping
    public BaseResponse<List<ChemicalResponseDTO>> getAllChemicals() {
        List<ChemicalResponseDTO> chemicals = chemicalService.getAllChemicals();
        return BaseResponse.ok(chemicals);
    }

    @Operation(summary = "Get chemical details by ID")
    @GetMapping("/{id}")
    public BaseResponse<ChemicalResponseDTO> getDetailChemical(@PathVariable("id") Long id) {
        ChemicalResponseDTO chemical = chemicalService.findDetailsById(id);
        return BaseResponse.ok(chemical);
    }

    @Operation(summary = "Create a new chemical")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Chemical> createChemical(@Valid @RequestBody ChemicalCreateRequestDTO request) {
        Chemical savedChemical = chemicalService.save(request);
        return BaseResponse.created(savedChemical);
    }

    @PutMapping("/update/{id}")
    public BaseResponse<Chemical> updateChemical(@PathVariable("id") Long id,
            @RequestBody ChemicalUpdateRequestDTO request) {
        log.info("request to update customer with id:  " + id);
        Chemical updatedChemical = chemicalService.update(id, request);
        return BaseResponse.ok(updatedChemical);
    }

    @Operation(summary = "Delete chemical by ID")
    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteChemical(@PathVariable("id") Long id) {
        log.info("request to delete chemical with id:  " + id);
        chemicalService.delete(id);
        return BaseResponse.ok(null);
    }

}
