package com.example.businessservice.domain.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Customer customer;
    private Integer transport_id;
    private Integer status;
    private Long total;
    private String note;
    private Date order_time;
}
