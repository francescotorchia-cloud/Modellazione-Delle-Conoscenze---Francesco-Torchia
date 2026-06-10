package it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

import java.util.Random;

public class BossStrategy implements CombatStrategy {

    private static final long serialVersionUID = 1L;

    private final Random random = new Random();
    private boolean nebbiaInCorso;

    @Override
    public MonsterIntent mossaSuccessiva(Player player, Monster monster) {
        if (nebbiaInCorso) {
            nebbiaInCorso = false;
            return MonsterIntent.CURA;
        }
        int tiro = random.nextInt(100);
        if (tiro < 45) {
            return MonsterIntent.ATTACCO_PESANTE;
        }
        if (tiro < 75) {
            nebbiaInCorso = true;
            return MonsterIntent.NEBBIA;
        }
        return MonsterIntent.CURA;
    }
}