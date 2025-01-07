package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {
    private String productId;
    private String categoryId;
    private String prodName;
    private String productDescription;
    private Double prodMarketPrice;
    private String imagePath;
    private int counts;
    //private LocalDateTime date;
}
