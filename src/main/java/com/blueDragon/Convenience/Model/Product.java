package com.blueDragon.Convenience.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Getter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private List<ConvenienceType> availableAt; // 살 수 있는 편의점

    @Column
    private List<FoodType> foodTypes;
}

