package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.FormaGeometrica;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.SkeletonStrategy;

public class Skeleton extends Monster {

    private static final long serialVersionUID = 1L;

    public Skeleton() {
        super("Scheletro", 15, 4, 5, new SkeletonStrategy());
    }

    @Override
    public FormaGeometrica getFormaGeometrica() {
        return FormaGeometrica.RETTANGOLO;
    }

    @Override
    public String getColoreEsadecimale() {
        return "#ECEFF1";
    }
}