package com.blueDragon.Convenience.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Table
@Data
public class ConvenienceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

}
