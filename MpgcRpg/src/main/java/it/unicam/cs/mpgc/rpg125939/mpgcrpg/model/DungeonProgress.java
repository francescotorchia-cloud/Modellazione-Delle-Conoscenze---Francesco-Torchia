package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.io.Serializable;

public class DungeonProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    private int stanzaCorrente;

    public DungeonProgress() {
        this.stanzaCorrente = 0;
    }

    public DungeonProgress(int stanzaCorrente) {
        this.stanzaCorrente = stanzaCorrente;
    }

    public int getStanzaCorrente() {
        return stanzaCorrente;
    }

    public void setStanzaCorrente(int stanzaCorrente) {
        this.stanzaCorrente = stanzaCorrente;
    }

    public void avanza() {
        this.stanzaCorrente++;
    }
}