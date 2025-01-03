package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.RecommendBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendBoard, Long> {
    @Query("SELECT r FROM RecommendBoard r ORDER BY r.id DESC")
    List<RecommendBoard> findAllRecommendationOrderByIdDesc();

    // 좋아요 수를 내림차순으로 집계
    @Query("SELECT r FROM RecommendBoard r LEFT JOIN r.recommendLikes rl GROUP BY r.id ORDER BY COUNT(rl) DESC")
    List<RecommendBoard> findAllByOrderByLikeCountDesc();

    // 내가 좋아요 누른 레시피 불러오기
    @Query("SELECT rl.recommendBoard FROM RecommendLike rl WHERE rl.user.username = :username")
    List<RecommendBoard> findLikedRecommendationByUser(@Param("username") String username);

    // 내가 쓴 글 불러오기
    @Query("SELECT r FROM RecommendBoard r WHERE r.user.username = :username")
    List<RecommendBoard> findRecommendationByUser(@Param("username") String username);

    // 댓글 수를 내림차순으로 집계
    @Query("SELECT r FROM RecommendBoard r LEFT JOIN r.recommendComments rc GROUP BY r.id ORDER BY COUNT(rc) DESC")
    List<RecommendBoard> findAllByOrderByCommentCountDesc();

    @Query("SELECT p FROM RecommendBoard p WHERE p.totalPrice <= :maxPrice ORDER BY p.totalPrice DESC")
    List<RecommendBoard> findRecommendBoardsByMaxTotalPrice(@Param("maxPrice") int maxPrice);

    @Query(value = "SELECT * FROM RecommendBoard p WHERE FIND_IN_SET(:foodType, p.foodTypes) > 0", nativeQuery = true)
    List<RecommendBoard> findByFoodType(@Param("foodType") String foodType);

    @Query(value = "SELECT * FROM RecommendBoard p WHERE FIND_IN_SET(:availableAt, p.availableAt) > 0", nativeQuery = true)
    List<RecommendBoard> findByAvailableAt(@Param("availableAt") String availableAt);

    Optional<RecommendBoard> findById(Long id);
}
