package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDataDTO {
    private String productId;
    private String categoryId;
    private String prodName;
    private Double prodMarketPrice;
    private String productDescription;
    private String imagePath;
    private int quantity;
}