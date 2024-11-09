package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.RecommendBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendBoard, Long> {
    @Query("SELECT r FROM RecommendBoard r ORDER BY r.id DESC")
    List<RecommendBoard> findAllRecommendationOrderByIdDesc();
}
