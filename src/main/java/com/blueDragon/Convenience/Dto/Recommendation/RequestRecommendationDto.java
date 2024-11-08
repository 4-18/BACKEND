package com.blueDragon.Convenience.Dto.Recommendation;

import com.blueDragon.Convenience.Dto.Product.RecommendProductRequestDto;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class RequestRecommendationDto {
    @NotEmpty(message = "제목은 비워둘 수 없습니다.")
    private String title;
    @NotEmpty(message = "내용은 비워둘 수 없습니다.")
    private String content;
    @NotEmpty(message = "상품은 비워둘 수 없습니다.")
    private List<Integer> productList;
}
