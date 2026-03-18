package com.chemical.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateRequestDTO {
    private String name;
    private String email;
    private String phone_number;
    private Date birthday;
    private String address;
    private Integer type;
}
