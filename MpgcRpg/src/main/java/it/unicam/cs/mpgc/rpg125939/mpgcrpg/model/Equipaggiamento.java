package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.io.Serializable;

public interface Equipaggiamento extends Serializable {

    String getNome();

    String riepilogoStat();

    void equipaggia(Player player);

    Equipaggiamento equipaggiatoAttuale(Player player);
}