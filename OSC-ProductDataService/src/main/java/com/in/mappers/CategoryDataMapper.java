package com.in.mappers;

import com.in.dtos.CategoryDetailsDTO;
import com.in.kafka.avro.category.CategoryDetails;

public class CategoryDataMapper {
    public static CategoryDetails dtoToAvro(CategoryDetailsDTO dto){
        return CategoryDetails.newBuilder()
                .setCategoryName(dto.getCategoryName())
                .setImagePath(dto.getImagePath())
                .build();
    }
    public static CategoryDetailsDTO avroToDto(String categoryId, CategoryDetails categoryDetails){
        return new CategoryDetailsDTO(categoryId,categoryDetails.getCategoryName().toString(),categoryDetails.getImagePath().toString());
    }
}
