package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.ProductCommentDto;
import com.blueDragon.Convenience.Dto.Product.ResponseProductCommentDto;
import com.blueDragon.Convenience.Exception.UserNotExistException;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.ProductComment;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.ProductCommentRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCommentService {
    private final ProductCommentRepository productCommentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public ProductCommentDto addComment(String username, Long productId, ProductCommentDto commentDto) {
        // productId로 Product 엔티티 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // ProductComment에 Product 설정
        ProductComment productComment = ProductComment.builder()
                .product(product) // product 설정
                .user(user)
                .comment(commentDto.getComment())
                .build();

        productComment = productCommentRepository.save(productComment);
        return ProductCommentDto.entityToDto(productComment);
    }


    // 댓글 조회
    public ResponseProductCommentDto getComment(Long commentId) {
        ProductComment productComment = productCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        return ResponseProductCommentDto.entityToDto(productComment);
    }

    // 특정 상품에 대한 모든 댓글 조회
    public List<ResponseProductCommentDto> getCommentsByProductId(Long productId) {
        List<ProductComment> comments = productCommentRepository.findByProductId(productId);
        return comments.stream()
                .map(ResponseProductCommentDto::entityToDto)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public ProductCommentDto updateComment(Long commentId, ProductCommentDto commentDto) {
        ProductComment productComment = productCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        productComment.updateComment(commentDto.getComment());
        productCommentRepository.save(productComment);
        return ProductCommentDto.entityToDto(productComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        if (!productCommentRepository.existsById(commentId)) {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }
        productCommentRepository.deleteById(commentId);
    }
}