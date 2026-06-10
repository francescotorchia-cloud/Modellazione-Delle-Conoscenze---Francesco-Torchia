package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

public interface Equipaggiamento {

    String getNome();

    String riepilogoStat();

    void equipaggia(Player player);

    Equipaggiamento equipaggiatoAttuale(Player player);
}