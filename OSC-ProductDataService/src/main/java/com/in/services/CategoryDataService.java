package com.in.services;

import com.in.dtos.CategoryDetailsDTO;

import java.util.List;

public interface CategoryDataService {
    List<CategoryDetailsDTO> getAllCategories();
}
