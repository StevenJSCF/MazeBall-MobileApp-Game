package com.example.SavingObjects.module;

import javax.persistence.*;

@Entity
public class Ball {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float x;
    private float y;

    @OneToOne(mappedBy = "ball")
    private BallState currentBallState;

}
