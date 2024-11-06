package com.blueDragon.Convenience.Model;

import com.blueDragon.Convenience.Converter.ConvenienceTypeListConverter;
import com.blueDragon.Convenience.Converter.FoodTypeListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    private String price;

    @Convert(converter = ConvenienceTypeListConverter.class) // 컨버터 적용
    private List<ConvenienceType> availableAt;

    @Convert(converter = FoodTypeListConverter.class) // 컨버터 적용
    private List<FoodType> foodTypes;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductLike> productLikes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductHate> productHates = new ArrayList<>();


    public static Product customBuilder(String name, String price, List<ConvenienceType> availableAt, List<FoodType> foodTypes, String imageUrl) {
        return Product.builder()
                .name(name)
                .price(price)
                .availableAt(availableAt)
                .foodTypes(foodTypes)
                .imageUrl(imageUrl)
                .build();
    }

    public Product setAvailableAt(List<ConvenienceType> availableAt) {
        this.availableAt = availableAt;
        return this;
    }
}