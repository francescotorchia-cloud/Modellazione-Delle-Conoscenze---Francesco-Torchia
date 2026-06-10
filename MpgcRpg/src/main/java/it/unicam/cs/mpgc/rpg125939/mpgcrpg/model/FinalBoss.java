package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.FormaGeometrica;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.BossStrategy;

public class FinalBoss extends Monster {

    private static final long serialVersionUID = 1L;

    public FinalBoss() {
        super("Fabrizio Fornarni", 150, 30, 0, new BossStrategy());
    }

    @Override
    public FormaGeometrica getFormaGeometrica() {
        return FormaGeometrica.POLIGONO;
    }

    @Override
    public String getColoreEsadecimale() {
        return "#6C3483";
    }
}