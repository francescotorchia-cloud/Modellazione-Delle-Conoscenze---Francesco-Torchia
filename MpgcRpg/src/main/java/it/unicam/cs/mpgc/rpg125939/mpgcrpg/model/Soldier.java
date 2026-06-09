package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.SoldierStrategy;

public class Soldier extends Monster {

    private static final long serialVersionUID = 1L;

    public Soldier() {
        super("Soldato Corrotto", 40, 10, 15, new SoldierStrategy());
    }
}