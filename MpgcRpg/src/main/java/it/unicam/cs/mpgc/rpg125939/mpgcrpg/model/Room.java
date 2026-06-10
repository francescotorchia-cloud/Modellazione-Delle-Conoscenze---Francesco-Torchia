package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Room {

    private final RoomType tipo;
    private final String nome;
    private final Supplier<List<Monster>> fornitoreMostri;
    private final NPC npc;

    private Room(RoomType tipo, String nome, Supplier<List<Monster>> fornitoreMostri, NPC npc) {
        this.tipo = tipo;
        this.nome = nome;
        this.fornitoreMostri = fornitoreMostri;
        this.npc = npc;
    }

    public static Room combattimento(String nome, Supplier<List<Monster>> fornitoreMostri) {
        return new Room(RoomType.COMBATTIMENTO, nome, fornitoreMostri, null);
    }

    public static Room boss(String nome, Supplier<List<Monster>> fornitoreMostri) {
        return new Room(RoomType.BOSS, nome, fornitoreMostri, null);
    }

    public static Room dialogo(NPC npc) {
        return new Room(RoomType.DIALOGO, npc.getNome(), null, npc);
    }

    public static Room santuario(String nome) {
        return new Room(RoomType.SANTUARIO, nome, null, null);
    }

    public static Room vittoria() {
        return new Room(RoomType.VITTORIA, "Vittoria", null, null);
    }

    public RoomType getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public List<Monster> generaMostri() {
        if (fornitoreMostri == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(fornitoreMostri.get());
    }

    public NPC getNpc() {
        return npc;
    }
}