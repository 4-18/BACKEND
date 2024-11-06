package com.blueDragon.Convenience.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ProductLike extends ReactionBaseEntity {
    public ProductLike(Product product, User user) {
        super(product, user);
    }


    // Like 클래스에만 필요한 필드와 메서드가 있다면 추가
}

