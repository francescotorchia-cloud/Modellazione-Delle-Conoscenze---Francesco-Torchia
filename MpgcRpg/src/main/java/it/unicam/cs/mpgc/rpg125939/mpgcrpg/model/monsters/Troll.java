package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.TrollStrategy;

public class Troll extends Monster {

    private static final long serialVersionUID = 1L;

    public Troll() {
        super("Troll di Caverna", 70, 22, 30, new TrollStrategy());
    }
}