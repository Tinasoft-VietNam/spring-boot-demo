package com.chemical.dto.response;

import com.chemical.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO extends BaseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone_number;
    private Date birthday;
    private String address;
    private Integer type;
    private List<OrderResponseDTO> orders;
}
