package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {
    private List<CategoryDetailsDTO> categories;
    private List<ProductDetailsDTO> featuredProducts;
    private List<ProductDetailsDTO> recentlyViewedProducts;
    private List<ProductDetailsDTO> similarProducts;
}
