package com.in.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_data")
public class CartDataEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "cart_products")
    private String cartProducts;

}
