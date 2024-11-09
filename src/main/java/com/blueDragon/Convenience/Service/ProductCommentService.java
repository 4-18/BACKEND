package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.ProductCommentDto;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.ProductComment;
import com.blueDragon.Convenience.Repository.ProductCommentRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
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


    // 댓글 작성
    @Transactional
    public ProductCommentDto addComment(Long productId, ProductCommentDto commentDto) {
        // productId로 Product 엔티티 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // ProductComment에 Product 설정
        ProductComment productComment = ProductComment.builder()
                .product(product) // product 설정
                .comment(commentDto.getComment())
                .build();

        productComment = productCommentRepository.save(productComment);
        return ProductCommentDto.entityToDto(productComment);
    }


    // 댓글 조회
    public ProductCommentDto getComment(Long commentId) {
        ProductComment productComment = productCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        return ProductCommentDto.entityToDto(productComment);
    }

    // 특정 상품에 대한 모든 댓글 조회
    public List<ProductCommentDto> getCommentsByProductId(Long productId) {
        List<ProductComment> comments = productCommentRepository.findByProductId(productId);
        return comments.stream()
                .map(ProductCommentDto::entityToDto)
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
