package com.example.businessservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String description;

    // Trong Domain Driven Design (DDD), thường ta chỉ lưu ID của entity con
    // hoặc thiết kế lại Aggregate Root để tránh rườm rà.
    // Tạm thời có thể bỏ qua List<ChemicalCategory> ở đây nếu nó không phục vụ logic nghiệp vụ cốt lõi của Category.
}
