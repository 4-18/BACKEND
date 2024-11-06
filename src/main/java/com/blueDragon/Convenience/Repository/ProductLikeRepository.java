package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
}
