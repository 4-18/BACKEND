package com.blueDragon.Convenience.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RecommendLike extends ReactionBaseEntity{
    @ManyToOne
    @JoinColumn(name = "recommend_board_id")
    private RecommendBoard recommendBoard;  // Notice the field name in lowercase (Java naming convention)

    public RecommendLike(RecommendBoard recommendBoard, User user) {
        super(user);
        this.recommendBoard = recommendBoard;
    }
}
