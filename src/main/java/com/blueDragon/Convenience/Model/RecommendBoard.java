package com.blueDragon.Convenience.Model;

import com.blueDragon.Convenience.Converter.ConvenienceTypeListConverter;
import com.blueDragon.Convenience.Converter.FoodTypeListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Getter
@Builder
public class RecommendBoard extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user; //작성자


    @Column(nullable = false)
    private String title; //제목

    @Lob
    @Column(nullable = false)
    private String content; //레시피 내용

    @Column
    private String imageUrl;

    @Convert(converter = ConvenienceTypeListConverter.class) // 컨버터 적용
    private List<ConvenienceType> availableAt;

    @Convert(converter = FoodTypeListConverter.class) // 컨버터 적용
    private List<FoodType> foodTypes;

    @Column(name = "total_price")
    private Integer totalPrice; //레시피 총 가격

    @OneToMany(mappedBy = "RecommendBoard", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "RecommendLike", cascade = CascadeType.ALL)
    private List<RecommendLike> recommendLikes = new ArrayList<>();

}
