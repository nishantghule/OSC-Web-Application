package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchProductResponseDTO {
    private String prodId;
    private String catId;
    private String prodName;
    private String prodDesc;
    private Double prodPrice;
    private List<SimilarProductsDTO> similarProducts;
}
