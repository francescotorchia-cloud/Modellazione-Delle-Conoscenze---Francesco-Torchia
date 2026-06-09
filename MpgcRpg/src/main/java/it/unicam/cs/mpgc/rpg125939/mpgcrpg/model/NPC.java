package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.util.ArrayList;
import java.util.List;

public class NPC {

    private final String nome;
    private final List<String> battute;
    private int indice;

    public NPC(String nome, List<String> battute) {
        this.nome = nome;
        this.battute = new ArrayList<>(battute);
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

    public void reset() {
        this.indice = 0;
    }

    public String getNome() {
        return nome;
    }
}