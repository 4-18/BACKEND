package com.blueDragon.Convenience.Dto.Recommendation;


import com.blueDragon.Convenience.Model.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRecommendationDto {
    private Long id;
    private User user;
    private String title;
    private LocalDateTime createdAt;
    private String content;
    private String imageUrls;
    private List<String> availableAt;
    private List<String> foodTypes;
    private String totalPrice;
    private Integer countLikes;
    private List<Product> productList;
    private Integer countComments;

    public static ResponseRecommendationDto entityToCreateDto(RecommendBoard recommendBoard) {
        return ResponseRecommendationDto.builder()
                .id(recommendBoard.getId())
                .title(recommendBoard.getTitle())
                .content(recommendBoard.getContent())
                .createdAt(recommendBoard.getCreatedAt())
                .countLikes(0)
                .countLikes(0)
                .foodTypes(recommendBoard.getFoodTypes().stream()
                        .map(FoodType::name) // Convert FoodType enum values to strings
                        .collect(Collectors.toList()))
                .availableAt(recommendBoard.getAvailableAt().stream()
                        .map(ConvenienceType::name)
                        .collect(Collectors.toList()))
                .imageUrls(recommendBoard.getImageUrl())
                .build();
    }
}