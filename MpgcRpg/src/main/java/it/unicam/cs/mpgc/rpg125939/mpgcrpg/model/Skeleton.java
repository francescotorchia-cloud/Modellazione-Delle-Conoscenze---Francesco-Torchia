package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.SkeletonStrategy;

public class Skeleton extends Monster {

    private static final long serialVersionUID = 1L;

    public Skeleton() {
        super("Scheletro", 15, 4, 5, new SkeletonStrategy());
    }
}