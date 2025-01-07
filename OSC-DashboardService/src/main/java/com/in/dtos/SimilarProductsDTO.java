package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarProductsDTO {
    private String productId;
    private String categoryId;
    private String prodName;
    private Double prodMarketPrice;
}
