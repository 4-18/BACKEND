package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.FoodTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodTypeEntity, Long> {
    boolean existsByName(String name);

    FoodTypeEntity findByName(String name);
}
