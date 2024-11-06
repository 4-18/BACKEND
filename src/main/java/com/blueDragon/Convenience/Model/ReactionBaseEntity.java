package com.blueDragon.Convenience.Model;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public abstract class ReactionBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    public ReactionBaseEntity(Product product, User user) {
        this.product = product;
        this.user = user;
    }
    // 공통 필드, 생성자, getter, setter 등
}