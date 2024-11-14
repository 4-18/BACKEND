package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.RecommendationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationCommentRepository extends JpaRepository<RecommendationComment, Long> {
    List<RecommendationComment> findByRecommendBoardId(Long recommendBoard);
}

