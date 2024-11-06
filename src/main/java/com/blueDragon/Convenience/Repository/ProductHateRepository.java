package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.ProductHate;
import com.blueDragon.Convenience.Model.ProductLike;
import com.blueDragon.Convenience.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHateRepository extends JpaRepository<ProductHate, Long> {
    boolean existsByProductAndUser(Product product, User user);
    void deleteByProductAndUser(Product product, User user);
}
