package it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

import java.util.Random;

public class TrollStrategy implements CombatStrategy {

    private static final long serialVersionUID = 1L;

    private final Random random = new Random();
    private boolean caricaInCorso;

    @Override
    public MonsterIntent mossaSuccessiva(Player player, Monster monster) {
        if (caricaInCorso) {
            caricaInCorso = false;
            return MonsterIntent.VULNERABILE;
        }
        int tiro = random.nextInt(100);
        if (tiro < 50) {
            caricaInCorso = true;
            return MonsterIntent.ATTACCO_PESANTE;
        }
        if (tiro < 80) {
            return MonsterIntent.ATTACCO_STANDARD;
        }
        return MonsterIntent.ATTACCO_AGILE;
    }
}