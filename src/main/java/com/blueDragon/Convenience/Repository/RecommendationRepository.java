package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.RecommendBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendBoard, Long> {
    @Query("SELECT r FROM RecommendBoard r ORDER BY r.id DESC")
    List<RecommendBoard> findAllRecommendationOrderByIdDesc();

    // 좋아요 수를 내림차순으로 집계
    @Query("SELECT r FROM RecommendBoard r LEFT JOIN r.recommendLikes rl GROUP BY r.id ORDER BY COUNT(rl) DESC")
    List<RecommendBoard> findAllByOrderByLikeCountDesc();

    @Query("SELECT COUNT(rl) FROM RecommendLike rl WHERE rl.recommendBoard.id = :boardId")
    int findLikeCountByBoardId(@Param("boardId") Long boardId);
}
