package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.RecommendationCommentDto;
import com.blueDragon.Convenience.Model.RecommendBoard;
import com.blueDragon.Convenience.Model.RecommendationComment;
import com.blueDragon.Convenience.Repository.RecommendationCommentRepository;
import com.blueDragon.Convenience.Repository.RecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationCommentService {

    private final RecommendationCommentRepository recommendationCommentRepository;
    private final RecommendationRepository recommendationBoardRepository;

    // 댓글 작성
    @Transactional
    public RecommendationCommentDto addComment(Long boardId, RecommendationCommentDto commentDto) {
        // boardId로 RecommendBoard 엔티티 조회
        RecommendBoard recommendBoard = recommendationBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("추천 게시글을 찾을 수 없습니다."));

        // RecommendationComment에 RecommendBoard 설정
        RecommendationComment recommendationComment = RecommendationComment.builder()
                .recommendBoard(recommendBoard) // RecommendBoard 설정
                .comment(commentDto.getComment())
                .build();

        // 댓글 저장
        recommendationComment = recommendationCommentRepository.save(recommendationComment);
        return RecommendationCommentDto.entityToDto(recommendationComment);
    }

    // 특정 추천 게시글의 모든 댓글 조회
    public List<RecommendationCommentDto> getCommentsByBoardId(Long boardId) {
        // boardId로 댓글 리스트 조회
        List<RecommendationComment> comments = recommendationCommentRepository.findByRecommendBoardId(boardId);

        // 댓글 리스트를 DTO로 변환하여 반환
        return comments.stream()
                .map(RecommendationCommentDto::entityToDto)
                .collect(Collectors.toList());
    }

    // 특정 댓글 조회
    public RecommendationCommentDto getComment(Long commentId) {
        RecommendationComment recommendationComment = recommendationCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        return RecommendationCommentDto.entityToDto(recommendationComment);
    }

    // 댓글 수정
    @Transactional
    public RecommendationCommentDto updateComment(Long commentId, RecommendationCommentDto commentDto) {
        // 댓글 조회
        RecommendationComment recommendationComment = recommendationCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 댓글 내용 수정
        recommendationComment.updateComment(commentDto.getComment());

        // 수정된 댓글 저장
        recommendationComment = recommendationCommentRepository.save(recommendationComment);

        return RecommendationCommentDto.entityToDto(recommendationComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        // 댓글 존재 여부 확인
        if (!recommendationCommentRepository.existsById(commentId)) {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }

        // 댓글 삭제
        recommendationCommentRepository.deleteById(commentId);
    }
}
