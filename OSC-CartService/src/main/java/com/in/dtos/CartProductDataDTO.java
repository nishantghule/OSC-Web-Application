package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductDataDTO {
    private String productId;
    private String productName;
    private String categoryId;
    private Double productPrice;
    private int quantity;
}