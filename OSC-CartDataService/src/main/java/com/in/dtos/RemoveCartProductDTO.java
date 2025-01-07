package com.in.dtos;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemoveCartProductDTO {
    private String userId;
    private String productId;
}
