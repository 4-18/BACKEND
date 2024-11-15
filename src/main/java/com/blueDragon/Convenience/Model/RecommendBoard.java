package com.blueDragon.Convenience.Model;


import com.blueDragon.Convenience.Converter.ConvenienceTypeListConverter;
import com.blueDragon.Convenience.Converter.FoodTypeListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL)
    @Convert(converter = ConvenienceTypeListConverter.class)
    @Column(name = "availableAt")
    private List<String> availableAt = new ArrayList<>();

    @JsonIgnore
    @Convert(converter = FoodTypeListConverter.class)
    //@OneToMany(cascade = CascadeType.ALL)
    private List<String> foodTypes = new ArrayList<>();


    @Column(name = "total_price")
    private Integer totalPrice; //레시피 총 가격

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_board_id") // 추천 보드와 제품을 연결하는 외래 키
    private List<Product> productList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recommendBoard", cascade = CascadeType.ALL, orphanRemoval = true)  // Reference the field name in RecommendLike
    private List<RecommendLike> recommendLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recommendBoard", cascade = CascadeType.ALL, orphanRemoval = true)  // Reference the field name in RecommendLike
    private List<RecommendationComment> recommendComments = new ArrayList<>();

}
