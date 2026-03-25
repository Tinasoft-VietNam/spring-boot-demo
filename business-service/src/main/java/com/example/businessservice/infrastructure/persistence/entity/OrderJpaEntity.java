package com.example.businessservice.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderJpaEntity extends BaseJpaEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CustomerJpaEntity customer;

    @Column
    private Integer transport_id;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private Long total;

    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private Date order_time;
}
