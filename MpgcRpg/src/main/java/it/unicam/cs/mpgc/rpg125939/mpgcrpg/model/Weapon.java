package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.io.Serializable;

public class Weapon implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int bonusDamage;
    private final int bonusLuck;

    public Weapon(String nome, int bonusDamage, int bonusLuck) {
        this.nome = nome;
        this.bonusDamage = bonusDamage;
        this.bonusLuck = bonusLuck;
    }

    public String getNome() {
        return nome;
    }

    public int getBonusDamage() {
        return bonusDamage;
    }

    public int getBonusLuck() {
        return bonusLuck;
    }
}