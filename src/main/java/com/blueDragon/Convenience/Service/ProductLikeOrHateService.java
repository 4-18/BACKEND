package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Exception.InvalidValueException;
import com.blueDragon.Convenience.Exception.ProductNotExistException;
import com.blueDragon.Convenience.Exception.UserNotExistException;
import com.blueDragon.Convenience.Model.*;
import com.blueDragon.Convenience.Repository.ProductHateRepository;
import com.blueDragon.Convenience.Repository.ProductLikeRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Repository.UserRepository;
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

    public void register(String username, Long id, String reaction) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotExistException("존재하지 않는 상품입니다."));

        if (!(reaction.equalsIgnoreCase("like")) || reaction.equalsIgnoreCase("hate")) {
            throw new InvalidValueException("잘못된 값을 입력했습니다.");
        }

        if (reaction.equalsIgnoreCase("like")) {
            ProductLike entity = (ProductLike) ProductLike.builder()
                    .product(product)
                    .user(user)
                    .build();

            productLikeRepository.save(entity);
        } else if (reaction.equalsIgnoreCase("hate")) {
            ProductHate entity = (ProductHate) ProductHate.builder()
                    .product(product)
                    .user(user)
                    .build();
            productHateRepository.save(entity);
        }
    }
}
