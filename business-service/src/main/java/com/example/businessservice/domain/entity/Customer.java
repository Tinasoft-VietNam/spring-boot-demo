package com.example.businessservice.domain.entity;

import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Long id;
    private String name;
    private String email;
    private String phone_number;
    private Date birthday;
    private String address;
    private Integer type;
    
    private List<Order> orders;
}
