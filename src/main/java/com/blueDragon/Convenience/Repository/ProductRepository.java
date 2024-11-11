package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.name = :name AND p.price = :price")
    boolean existsByNameAndPrice(@Param("name") String name, @Param("price") String price);

    @Query("SELECT p FROM Product p WHERE CAST(REPLACE(p.price, ',', '') AS integer) <= :maxPrice ORDER BY CAST(REPLACE(p.price, ',', '') AS integer) DESC")
    List<Product> findProductsByMaxPrice(@Param("maxPrice") int maxPrice);

    @Query("SELECT p FROM Product p ORDER BY p.id DESC")
    List<Product> findAllProductsOrderByIdDesc();

    @Query("SELECT pl.product FROM ProductLike pl WHERE pl.user = :user")
    List<Product> findLikedProductsByUser(@Param("user") User user);
    @Query("SELECT pl.product FROM ProductLike pl WHERE pl.user.username = :username")
    List<Product> findLikedProductsByUser(@Param("username") String username);
}
