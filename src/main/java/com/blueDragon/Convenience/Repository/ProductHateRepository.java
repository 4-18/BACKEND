package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.ProductHate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHateRepository extends JpaRepository<ProductHate, Long> {
}
