package it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

public interface CombatStrategy {

    MonsterIntent mossaSuccessiva(Player player, Monster monster);
}