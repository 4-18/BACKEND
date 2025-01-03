package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Exception.CategoryInvalidValueException;
import com.blueDragon.Convenience.Exception.ConvenienceInvalidValueException;
import com.blueDragon.Convenience.Exception.EmptyException;
import com.blueDragon.Convenience.Exception.ProductNotExistException;
import com.blueDragon.Convenience.Model.ConvenienceEntity;
import com.blueDragon.Convenience.Model.FoodTypeEntity;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductCommentRepository;
import com.blueDragon.Convenience.Repository.ProductLikeRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final CategoryService categoryService;
    private final ConvenienceService convenienceService;
    private final ProductCommentRepository productCommentRepository;

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

    public List<ProductDto> getLikedProductsByUser(String username) {
        List<Product> productList = productRepository.findLikedProductsByUser(username);
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


    public int sumProductPrices(List<Product> products) {
        return products.stream()
                .mapToInt(product -> {
                    String priceStr = product.getPrice().replace(",", ""); // Remove commas from price
                    return Integer.parseInt(priceStr); // Convert to integer
                })
                .sum();
    }

    public List<ProductDto> getProductByPopular() {
        List<Product> list = productRepository.findProductsOrderedByLikes();
        return list.stream().map((product -> {
            ProductDto dto = ProductDto.entityToDto(product);
            dto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
            return dto;
        })).collect(Collectors.toList());
    }

    public List<ProductDto> getProductByCommentPopular() {
        List<Product> list = productRepository.findAllByOrderByCommentCountDesc();
        return list.stream().map((product -> {
            ProductDto dto = ProductDto.entityToDto(product);
            dto.setCountComments(productCommentRepository.countProductCommentsByProductId(product.getId()));
            return dto;
        })).collect(Collectors.toList());
    }

    public List<ProductDto> getProductByCategory(String category) {
        // Convert category string to FoodType enum
        if (categoryService.getProductByCategory(category)) {
            // Get products by food type and return mapped DTO list
            List<Product> products = productRepository.findByFoodType(category);
            if (products.isEmpty()) {
                throw new EmptyException("요청은 잘 됐는데 빔");
            }

            return products.stream().map(product -> {
                ProductDto dto = ProductDto.entityToDto(product);
                dto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
                return dto;
            }).collect(Collectors.toList());
        } else {
            throw new CategoryInvalidValueException("카테고리가 잘못 선택되었습니다.");
       }

    }

    public List<ProductDto> getProductsByConvenience(String name) {
        if (convenienceService.getCovenience(name)) {
            List<Product> products = productRepository.findByAvailableAt(name);
            return products.stream().map(product -> {
                ProductDto dto = ProductDto.entityToDto(product);
                dto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
                return dto;
            }).collect(Collectors.toList());
        } else {
            throw new ConvenienceInvalidValueException("편의점이 잘못 선택되었습니다.");
        }
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotExistException("존재하지 않는 상품입니다."));

        return ProductDto.entityToDto(product);
    }
}
