package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Exception.CategoryInvalidValueException;
import com.blueDragon.Convenience.Model.FoodTypeEntity;
import com.blueDragon.Convenience.Repository.FoodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final FoodTypeRepository foodTypeRepository;

    public FoodTypeEntity getProductByCategory(String category) {
        try {
            // Try to find the corresponding FoodType enum
            return foodTypeRepository.findByName(category);
        } catch (IllegalArgumentException e) {
            // Throw custom exception if the category is invalid
            throw new CategoryInvalidValueException("Invalid category: " + category);
        }
    }
}