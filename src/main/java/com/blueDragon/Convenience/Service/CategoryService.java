package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Exception.CategoryInvalidValueException;
import com.blueDragon.Convenience.Model.FoodTypeEntity;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.FoodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final FoodTypeRepository foodTypeRepository;

    public boolean getProductByCategory(String category) {
        try {
            // Try to find the corresponding FoodType enum
            return foodTypeRepository.existsByName(category);
        } catch (IllegalArgumentException e) {
            // Throw custom exception if the category is invalid
            throw new CategoryInvalidValueException("Invalid category: " + category);
        }
    }

    public List<String> combineFoodTypes(List<Product> products) {
        return products.stream()
                .flatMap(product -> product.getFoodTypes().stream()) // Assuming each Product has a List<FoodType>
                .distinct()
                .collect(Collectors.toList());
    }

}