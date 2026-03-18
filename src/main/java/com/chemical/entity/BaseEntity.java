package com.chemical.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @Column
    private Date created_at = new Date();

    @LastModifiedDate
    @Column
    private Date updated_at = new Date();

    @JsonIgnore
    @CreatedBy
    @Column
    private String created_by;

    @JsonIgnore
    @LastModifiedBy
    @Column
    private String updated_by;
}

