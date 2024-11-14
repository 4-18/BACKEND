package com.blueDragon.Convenience.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Getter
@Builder
public class RecommendationComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String comment; //댓글 본문

    @ManyToOne
    @JoinColumn(name = "recommend_board_id", nullable = false) // FK 설정
    private RecommendBoard recommendBoard; // 필드 이름 확인

    //생성 시간은 baseEntity에 존재 (중복되는 코드 줄이기 위함)
    public void updateComment(String newComment) {
        this.comment = newComment;
    }
}

