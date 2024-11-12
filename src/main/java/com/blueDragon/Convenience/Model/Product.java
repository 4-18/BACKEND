package com.blueDragon.Convenience.Model;


import com.blueDragon.Convenience.Converter.ConvenienceTypeListConverter;
import com.blueDragon.Convenience.Converter.FoodTypeListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String price;

    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL)
    @Convert(converter = ConvenienceTypeListConverter.class)
    @Column(name = "availableAt")
    private List<String> availableAt = new ArrayList<>();

    @JsonIgnore
    @Convert(converter = FoodTypeListConverter.class)
    //@OneToMany(cascade = CascadeType.ALL)
    private List<String> foodTypes = new ArrayList<>();

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductLike> productLikes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "recommend_board_id")
    private RecommendBoard recommendBoard;


    @Builder
    public static Product customBuilder(String name, String price, List<String> availableAt, List<String> foodTypes, String imageUrl) {
        return Product.builder()
                .name(name)
                .price(price)
                .availableAt(availableAt)
                .foodTypes((foodTypes))
                .imageUrl(imageUrl)
                .build();
    }

    public Product setAvailableAt(List<String> availableAt) {
        this.availableAt = availableAt;
        return this;
    }

    public void setRecommendBoard(RecommendBoard entity) {
        this.recommendBoard = entity;
    }
}