package com.chemical.dto.response;

import com.chemical.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderResponseDTO extends BaseDTO {
    private Long id;
    private Integer customer_id;
    private Integer transport_id;
    private Integer status;
    private Long total;
    private String note;
    private Date order_time;
}
