package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.FormaGeometrica;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.CombatStrategy;

import java.io.Serializable;

public abstract class Monster implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int maxHp;
    private final int baseDamage;
    private final int fragmentReward;

    private int hp;

    private final CombatStrategy strategy;

    protected Monster(String nome, int maxHp, int baseDamage, int xpValue, CombatStrategy strategy) {
        this.nome = nome;
        this.maxHp = maxHp;
        this.baseDamage = baseDamage;
        this.fragmentReward = xpValue;
        this.strategy = strategy;
        this.hp = maxHp;
    }

    public MonsterIntent decidiMossa(Player player) {
        return strategy.mossaSuccessiva(player, this);
    }

    public boolean isVivo() {
        return hp > 0;
    }

    public String getNome() {
        return nome;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getFragmentReward() {
        return fragmentReward;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public CombatStrategy getStrategy() {
        return strategy;
    }

    public void subisciSchivata() {
        // Di base non fa nulla, lo sovrascriverà solo il Soldato
    }

    public int calcolaDannoRicevuto(int dannoBase, MonsterIntent intentoCorrente) {
        return dannoBase;
    }

    public int forzaPrecisionePesante(int precisioneGiocatore) {
        return precisioneGiocatore;
    }

    public abstract FormaGeometrica getFormaGeometrica();

    public abstract String getColoreEsadecimale();
}