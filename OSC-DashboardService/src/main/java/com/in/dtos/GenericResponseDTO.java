package com.in.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"TYPE", "Featured Products", "Recently Viewed Products", "Similar Products", "Categories"})
public class GenericResponseDTO {
    private int code;
    private Object dataObject;
}
