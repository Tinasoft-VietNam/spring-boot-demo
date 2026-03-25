package com.example.businessservice.presentation.dto.response;

import com.example.businessservice.presentation.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerResponseDTO extends BaseDTO  {
    private Long id;
    private String name;
    private String address;
    private String website;
    private String contact_information;
}
