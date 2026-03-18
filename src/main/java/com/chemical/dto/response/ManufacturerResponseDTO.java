package com.chemical.dto.response;

import com.chemical.dto.BaseDTO;
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
