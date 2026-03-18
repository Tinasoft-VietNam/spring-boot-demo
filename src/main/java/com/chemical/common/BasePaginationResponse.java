package com.chemical.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasePaginationResponse<T> implements Serializable {

    private T data;
    private Integer page_number;
    private Integer total_pages;
    private Integer total_elements; //for-current-page
    private Integer status_code;
    private String message;

    public BasePaginationResponse(T data, Integer page_number, Integer total_pages, Integer total_elements, Integer status_code, String message) {
        this.data = data;
        this.page_number = page_number;
        this.total_pages = total_pages;
        this.total_elements = total_elements;
        this.status_code = status_code;
        this.message = message;
    }

    public static <T> BasePaginationResponse<T> ok(T data, Integer page_number, Integer total_pages, Integer total_elements) {
        return new BasePaginationResponse<>(data, page_number, total_pages, total_elements, HttpStatus.OK.value(), "success");
    }

}