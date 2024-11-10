package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.RecommendBoard;
import com.blueDragon.Convenience.Model.RecommendLike;
import com.blueDragon.Convenience.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationLikeRepository extends JpaRepository<RecommendLike, Long> {
    boolean existsByRecommendBoardAndUser(RecommendBoard recommendBoard, User user);
    void deleteByRecommendBoardAndUser(RecommendBoard recommendBoard, User user);
}
