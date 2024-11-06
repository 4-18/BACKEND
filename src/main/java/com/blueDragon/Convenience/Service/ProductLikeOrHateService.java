package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Exception.InvalidValueException;
import com.blueDragon.Convenience.Exception.ProductNotExistException;
import com.blueDragon.Convenience.Exception.UserNotExistException;
import com.blueDragon.Convenience.Model.*;
import com.blueDragon.Convenience.Repository.ProductHateRepository;
import com.blueDragon.Convenience.Repository.ProductLikeRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductLikeOrHateService {
    private final ProductHateRepository productHateRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public void register(String username, Long id, String reaction) {
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotExistException("존재하지 않는 상품입니다."));

        if (!(reaction.equalsIgnoreCase("like")) || reaction.equalsIgnoreCase("hate")) {
            throw new InvalidValueException("잘못된 값을 입력했습니다.");
        }

        // 만약에 이미 좋아요나 취소가 되어있다면?

        if (reaction.equalsIgnoreCase("like")) {
            if (productLikeRepository.existsByProductAndUser(product,user)) {
                // 그거 지움
                productLikeRepository.deleteByProductAndUser(product,user);
            }
            else {
            ProductLike entity = new ProductLike(product, user);
            productLikeRepository.save(entity); }

        } else if (reaction.equalsIgnoreCase("hate")) {
            if (productHateRepository.existsByProductAndUser(product, user)) {
                productHateRepository.deleteByProductAndUser(product,user);
            }
            else {
                ProductHate entity = new ProductHate(product, user);
                productHateRepository.save(entity);
            }
        }
    }
}
