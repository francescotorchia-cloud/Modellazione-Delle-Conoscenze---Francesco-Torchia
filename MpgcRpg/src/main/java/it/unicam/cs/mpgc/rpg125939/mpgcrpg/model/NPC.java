package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.util.ArrayList;
import java.util.List;

public class NPC {

    private final String nome;
    private final List<String> battute;
    private final Equipaggiamento dono;
    private int indice;

    public NPC(String nome, List<String> battute) {
        this(nome, battute, null);
    }

    public NPC(String nome, List<String> battute, Equipaggiamento dono) {
        this.nome = nome;
        this.battute = new ArrayList<>(battute);
        this.dono = dono;
        this.indice = 0;
    }

    public boolean haAltreBattute() {
        return indice < battute.size();
    }

    public String prossimaBattuta() {
        if (!haAltreBattute()) {
            return null;
        }
        return battute.get(indice++);
    }

    public boolean haDono() {
        return dono != null;
    }

    public Equipaggiamento getDono() {
        return dono;
    }

    public void reset() {
        this.indice = 0;
    }

    public String getNome() {
        return nome;
    }
}