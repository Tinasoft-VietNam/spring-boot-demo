package com.example.businessservice.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer {
    private Long id;
    private String name;
    private String address;
    private String website;
    private String contact_information;
}
