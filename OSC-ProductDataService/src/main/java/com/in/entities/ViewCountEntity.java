package com.in.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_view_count")
@Entity
public class ViewCountEntity {
    @Id
    private String productId;
    private String categoryId;
    private int viewCount;
}
