package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailsDTO {
    private String categoryId;
    private String categoryName;
    private String imagePath;
}
