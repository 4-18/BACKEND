package com.blueDragon.Convenience.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Entity
@Data
@Table
public class FoodTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

}
