package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.ConvenienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenienceEntityRepository extends JpaRepository<ConvenienceEntity, Long> {
    ConvenienceEntity findByName(String name);
    boolean existsByName(String name);
}
