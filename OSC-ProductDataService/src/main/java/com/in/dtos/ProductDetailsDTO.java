package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDTO {
    private String productId;
    private String categoryId;
    private String prodName;
    private String productDescription;
    private Double prodMarketPrice;
    private String imagePath;
    private int counts;
    private LocalDateTime date;

}
