package it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

import java.util.Random;

public class ZombieStrategy implements CombatStrategy {

    private static final long serialVersionUID = 1L;

    private final Random random = new Random();

    @Override
    public MonsterIntent mossaSuccessiva(Player player, Monster monster) {
        if (random.nextInt(100) < 60) {
            return MonsterIntent.DIFESA;
        }
        return MonsterIntent.ATTACCO_PESANTE;
    }
}