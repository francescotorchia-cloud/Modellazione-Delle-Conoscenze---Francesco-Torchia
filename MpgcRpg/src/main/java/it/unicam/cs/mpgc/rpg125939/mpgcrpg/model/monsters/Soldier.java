package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.FormaGeometrica;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.SoldierStrategy;

public class Soldier extends Monster {

    private static final long serialVersionUID = 1L;

    public Soldier() {
        super("Soldato Corrotto", 40, 10, 15, new SoldierStrategy());
    }

    @Override
    public void subisciSchivata() {
        ((it.unicam.cs.mpgc.rpg125939.mpgcrpg.strategy.SoldierStrategy) this.getStrategy()).registraSchivata();
    }

    @Override
    public int forzaPrecisionePesante(int precisioneGiocatore) {
        return 20;
    }

    @Override
    public FormaGeometrica getFormaGeometrica() {
        return FormaGeometrica.RETTANGOLO;
    }

    @Override
    public String getColoreEsadecimale() {
        return "#4A6FA5";
    }
}