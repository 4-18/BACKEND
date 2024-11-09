package com.blueDragon.Convenience.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ProductLike extends ReactionBaseEntity {
    @ManyToOne
    @JoinColumn(name = "Product")
    private Product product;

    public ProductLike(Product product, User user) {
        super(user);
        this.product = product;
    }
}

