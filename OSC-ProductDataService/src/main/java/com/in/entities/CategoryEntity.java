package com.in.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @Column(name = "categoryid")
    private String categoryId;

    @NotNull
    @Column(name = "categoryname")
    private String categoryName;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> productEntities;
}

