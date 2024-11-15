package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.Product;
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

    @Query("SELECT pl.product FROM ProductLike pl WHERE pl.user.username = :username")
    List<Product> findLikedProductsByUser(@Param("username") String username);

//    // 댓글 수를 내림차순으로 집계
//    @Query("SELECT r FROM RecommendBoard r LEFT JOIN r.recommendComments rc GROUP BY r.id ORDER BY COUNT(rc) DESC")
//    List<RecommendBoard> findAllByOrderByCommentCountDesc();

    // 댓글 수를 내림차순으로 집계
    @Query("SELECT p FROM  Product p LEFT JOIN p.productComments pc GROUP BY p.id ORDER BY COUNT(pc) DESC")
    List<Product> findAllByOrderByCommentCountDesc();

    @Query("SELECT p FROM Product p LEFT JOIN ProductLike pl ON pl.product = p " +
            "GROUP BY p ORDER BY COUNT(pl) DESC")
    List<Product> findProductsOrderedByLikes();

    @Query(value = "SELECT * FROM Product p WHERE FIND_IN_SET(:foodType, p.foodTypes) > 0", nativeQuery = true)
    List<Product> findByFoodType(@Param("foodType") String foodType);

    @Query(value = "SELECT * FROM Product p WHERE FIND_IN_SET(:availableAt, p.availableAt) > 0", nativeQuery = true)
    List<Product> findByAvailableAt(@Param("availableAt") String availableAt);
}
