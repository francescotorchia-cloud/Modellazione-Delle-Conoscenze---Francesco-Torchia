package it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

public class SkeletonStrategy implements CombatStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public MonsterIntent mossaSuccessiva(Player player, Monster monster) {
        return MonsterIntent.ATTACCO_STANDARD;
    }
}