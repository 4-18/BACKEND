package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.RecommendBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<RecommendBoard, Long> {

}
