package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.ProductComment;
import com.blueDragon.Convenience.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
    // 특정 상품에 대한 모든 댓글 조회
    List<ProductComment> findByProductId(Long productId);
}
