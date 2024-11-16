package com.blueDragon.Convenience.Dto.Recommendation;

import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Model.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class ResponseRecommendationDto {
    private Long id;
    private String nickname;
    private String title;
    private LocalDateTime createdAt;
    private List<String> content;
    private String imageUrls;
    private List<String> availableAt;
    private List<String> foodTypes;
    @Setter
    private List<ProductDto> productList;
    private String totalPrice;
    private int countLikes;
    private int countComments;

    public static ResponseRecommendationDto entityToCreateDto(RecommendBoard recommendBoard) {
        return ResponseRecommendationDto.builder()
                .id(recommendBoard.getId())
                .nickname(recommendBoard.getUser().getNickname())
                .title(recommendBoard.getTitle())
                .content(splitContentByNewLine(recommendBoard.getContent())) // 개행으로 분리
                .createdAt(recommendBoard.getCreatedAt())
                .countLikes(0)
                .countComments(0)
                .totalPrice(String.format("%,d", recommendBoard.getTotalPrice()))
                .foodTypes(recommendBoard.getFoodTypes())
                .availableAt(recommendBoard.getAvailableAt())
                .imageUrls(recommendBoard.getImageUrl())
                .productList(recommendBoard.getProductList().stream().map(ProductDto::entityToDto).collect(Collectors.toList()))
                .build();
    }

    public static ResponseRecommendationDto entityToDto(RecommendBoard recommendBoard) {
        return ResponseRecommendationDto.builder()
                .id(recommendBoard.getId())
                .nickname(recommendBoard.getUser().getNickname())
                .title(recommendBoard.getTitle())
                .content(splitContentByNewLine(recommendBoard.getContent())) // 개행으로 분리
                .createdAt(recommendBoard.getCreatedAt())
                .countLikes(recommendBoard.getRecommendLikes().size())
                .countComments(recommendBoard.getRecommendComments().size())
                .totalPrice(String.format("%,d", recommendBoard.getTotalPrice()))
                .foodTypes(recommendBoard.getFoodTypes())
                .availableAt(recommendBoard.getAvailableAt())
                .productList(recommendBoard.getProductList().stream().map(ProductDto::entityToDto).collect(Collectors.toList()))
                .imageUrls(recommendBoard.getImageUrl())
                .build();
    }

    // 개행 문자로 String을 나눠 List<String>으로 변환하는 메서드
    private static List<String> splitContentByNewLine(String content) {
        if (content == null || content.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }
        return Arrays.stream(content.split("\\n")) // 개행으로 나누기
                .map(String::trim) // 각 줄 공백 제거
                .filter(line -> !line.isEmpty()) // 빈 줄 제거
                .collect(Collectors.toList());
    }
}
