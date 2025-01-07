package com.in.entities;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @Column(name = "productid")
    private String productId;

    @Column(name = "productname")
    private String prodName;

    @Column(name = "productprice")
    private Double prodMarketPrice;

    @Column(name = "productdescription")
    private String productDescription;

    @Column(name = "imagepath")
    private String imagePath;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryid")
    private CategoryEntity category;
}
