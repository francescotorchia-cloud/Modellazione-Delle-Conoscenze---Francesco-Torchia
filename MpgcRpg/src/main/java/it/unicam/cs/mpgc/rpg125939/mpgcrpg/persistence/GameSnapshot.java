package it.unicam.cs.mpgc.rpg125939.mpgcrpg.persistence;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.DungeonProgress;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

import java.io.Serializable;

public class GameSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Player player;
    private final DungeonProgress progress;

    public GameSnapshot(Player player, DungeonProgress progress) {
        this.player = player;
        this.progress = progress;
    }

    public Player getPlayer() {
        return player;
    }

    public DungeonProgress getProgress() {
        return progress;
    }
}