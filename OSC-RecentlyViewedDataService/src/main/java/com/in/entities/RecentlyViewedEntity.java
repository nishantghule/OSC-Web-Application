package com.in.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recently_viewed")
public class RecentlyViewedEntity {
    @Id
    private String userId;
    private List<String> productIds;
}
