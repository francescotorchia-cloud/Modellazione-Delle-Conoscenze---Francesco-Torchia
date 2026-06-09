package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.io.Serializable;

public class Armor implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int bonusHp;
    private final double bonusDodge;

    public Armor(String nome, int bonusHp, double bonusDodge) {
        this.nome = nome;
        this.bonusHp = bonusHp;
        this.bonusDodge = bonusDodge;
    }

    public String getNome() {
        return nome;
    }

    public int getBonusHp() {
        return bonusHp;
    }

    public double getBonusDodge() {
        return bonusDodge;
    }
}