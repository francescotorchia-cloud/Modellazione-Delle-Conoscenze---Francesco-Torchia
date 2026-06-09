package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.ZombieStrategy;

public class Zombie extends Monster {

    private static final long serialVersionUID = 1L;

    public Zombie() {
        super("Zombie", 30, 6, 10, new ZombieStrategy());
    }
    @Override
    public int calcolaDannoRicevuto(int dannoBase, MonsterIntent intentoCorrente) {
        int riduzione = (intentoCorrente == MonsterIntent.DIFESA) ? 6 : 3;
        return Math.max(0, dannoBase - riduzione);
    }
}