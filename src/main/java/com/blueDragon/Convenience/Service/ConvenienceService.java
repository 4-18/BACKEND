package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Exception.CategoryInvalidValueException;
import com.blueDragon.Convenience.Repository.ConvenienceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvenienceService {
    private final ConvenienceEntityRepository convenienceEntityRepository;

    public boolean getCovenience(String name) {
        try {
            // Try to find the corresponding FoodType enum
            return convenienceEntityRepository.existsByName(name);
        } catch (IllegalArgumentException e) {
            // Throw custom exception if the category is invalid
            throw new CategoryInvalidValueException("Invalid convenience: " + name);
        }
    }
}
