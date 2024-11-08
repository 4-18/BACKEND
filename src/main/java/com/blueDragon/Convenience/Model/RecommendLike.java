package com.blueDragon.Convenience.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RecommendLike extends ReactionBaseEntity{
    @ManyToOne
    @JoinColumn(name = "recommend_board")
    private RecommendBoard RecommendBoard;
}
