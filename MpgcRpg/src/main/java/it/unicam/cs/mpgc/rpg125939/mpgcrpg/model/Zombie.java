package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.ZombieStrategy;

public class Zombie extends Monster {

    private static final long serialVersionUID = 1L;

    public Zombie() {
        super("Zombie", 30, 6, 10, new ZombieStrategy());
    }
}