package com.blueDragon.Convenience.Dto.Recommendation;

import com.blueDragon.Convenience.Dto.Product.RecommendProductRequestDto;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRecommendationDto {
    private String title;
    private String content;
    private List<Integer> productList;
}
