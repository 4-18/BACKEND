package com.blueDragon.Convenience.Dto.Product;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String price;
    private List<String> availableAt;
    private String foodType;
    private String imageUrl;

    public static ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .foodType(product.getFoodTypes().toString())
                .availableAt(availableAtToString(product.getAvailableAt()))
                .build();
    }

    public static List<String> availableAtToString(List<ConvenienceType> availableAt) {
        return availableAt.stream().map((Enum::toString)).collect(Collectors.toList());
    }
}
