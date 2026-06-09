package it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

import java.util.Random;

public class SoldierStrategy implements CombatStrategy {

    private static final long serialVersionUID = 1L;

    private final Random random = new Random();
    private boolean schivataInSospeso;

    @Override
    public MonsterIntent mossaSuccessiva(Player player, Monster monster) {
        if (schivataInSospeso) {
            schivataInSospeso = false;
            return MonsterIntent.ATTACCO_AGILE;
        }
        if (random.nextInt(100) < 70) {
            return MonsterIntent.ATTACCO_STANDARD;
        }
        return MonsterIntent.RECUPERO;
    }

    public void registraSchivata() {
        this.schivataInSospeso = true;
    }
}