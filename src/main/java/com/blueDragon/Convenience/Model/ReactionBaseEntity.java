package com.blueDragon.Convenience.Model;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "reaction_type")
@Getter
@Builder
@AllArgsConstructor
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

    // 공통 필드, 생성자, getter, setter 등
}