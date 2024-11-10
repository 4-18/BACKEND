package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Exception.RecommendationNotExistException;
import com.blueDragon.Convenience.Exception.UserNotExistException;
import com.blueDragon.Convenience.Model.RecommendBoard;
import com.blueDragon.Convenience.Model.RecommendLike;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.RecommendationLikeRepository;
import com.blueDragon.Convenience.Repository.RecommendationRepository;
import com.blueDragon.Convenience.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationLikeService {
    private final RecommendationLikeRepository recommendationLikeRepository;
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void register(String username, Long id) {
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("존재하지 않는 유저입니다."));

        RecommendBoard recommendBoard = recommendationRepository.findById(id)
                .orElseThrow(() -> new RecommendationNotExistException("존재하지 않는 레시피입니다."));

        // 만약에 이미 좋아요가 되어있다면?

        if (recommendationLikeRepository.existsByRecommendBoardAndUser(recommendBoard, user)) {
            // 좋아요 취소
            recommendationLikeRepository.deleteByRecommendBoardAndUser(recommendBoard, user);
        }
        else {
            RecommendLike entity = new RecommendLike(recommendBoard, user);
            recommendationLikeRepository.save(entity);
        }
    }

}
