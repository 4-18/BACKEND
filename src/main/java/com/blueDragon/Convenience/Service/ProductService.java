package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Exception.EmptyException;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
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

//    public List<ProductDto> getList(String sort) {
//
//
//    }

    public List<ProductDto> getProductByPrice(int price) {
        List<Product> productList = productRepository.findProductsByMaxPrice(price);
        if (productList.isEmpty()) {
            throw new EmptyException("비어있습니다.");
        }
        return productList.stream().map((ProductDto::entityToDto)).collect(Collectors.toList());
    }

    public List<ProductDto> getProductByNewest() {
        List<Product> productList = productRepository.findAllProductsOrderByIdDesc();
        if (productList.isEmpty()) {
            throw new EmptyException("비어있습니다.");
        }
        return productList.stream().map((ProductDto::entityToDto)).collect(Collectors.toList());
    }

}
