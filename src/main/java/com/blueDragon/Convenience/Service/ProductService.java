package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Exception.EmptyException;
import com.blueDragon.Convenience.Exception.ProductNotExistException;
import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.FoodType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.ProductLikeRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;

//    public List<ProductDto> getList(String sort) {
//
//
//    }

    public List<ProductDto> getProductByPrice(int price) {
        List<Product> productList = productRepository.findProductsByMaxPrice(price);
        if (productList.isEmpty()) {
            throw new EmptyException("비어있습니다.");
        }
        return productList.stream().map((product -> {
            ProductDto dto = ProductDto.entityToDto(product);
            dto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
            return dto;
        })).collect(Collectors.toList());
    }

    public List<ProductDto> getProductByNewest() {
        List<Product> productList = productRepository.findAllProductsOrderByIdDesc();
        if (productList.isEmpty()) {
            throw new EmptyException("비어있습니다.");
        }
        return productList.stream().map((product -> {
            ProductDto dto = ProductDto.entityToDto(product);
            dto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
            return dto;
        })).collect(Collectors.toList());
    }

    public List<ProductDto> getLikedProductsByUser(User user) {
        List<Product> productList = productRepository.findLikedProductsByUser(user);
        if (productList.isEmpty()) {
            throw new EmptyException("비어있습니다.");
        }
        return productList.stream().map((product -> {
            ProductDto dto = ProductDto.entityToDto(product);
            dto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
            return dto;
        })).collect(Collectors.toList());
    }

    public List<Product> getProductFromDto(List<Integer> recommendProductRequestDtos) {
        return recommendProductRequestDtos.stream().map((recommendProductRequestDto -> productRepository.findById(Long.valueOf(recommendProductRequestDto))
                .orElseThrow(() -> new ProductNotExistException("존재하지 않는 상품 아이디입니다.")))).collect(Collectors.toList());
    }

    public List<FoodType> combineFoodTypes(List<Product> products) {
        return products.stream()
                .flatMap(product -> product.getFoodTypes().stream()) // Assuming each Product has a List<FoodType>
                .distinct()
                .collect(Collectors.toList());
    }


    public List<ConvenienceType> combineConvenienceTypes(List<Product> products) {
        return products.stream()
                .flatMap(product -> product.getAvailableAt().stream()) // Assuming each Product has a List<FoodType>
                .distinct()
                .collect(Collectors.toList());
    }


    public int sumProductPrices(List<Product> products) {
        return products.stream()
                .mapToInt(product -> {
                    String priceStr = product.getPrice().replace(",", ""); // Remove commas from price
                    return Integer.parseInt(priceStr); // Convert to integer
                })
                .sum();
    }

}
