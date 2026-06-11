package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.util.List;
import java.util.Random;

public final class CatalogoEquipaggiamento {

    private CatalogoEquipaggiamento() {
    }

    private static final List<Weapon> ARMI = List.of(
            new Weapon("Pugnale Bilanciato", 5, 8, "Maneggevole e preciso."),
            new Weapon("Spada del Mercenario", 8, 0, "Affidabile e diretta."),
            new Weapon("Martello Brutale", 13, -16, "Devastante, ma difficile da piazzare."),
            new Weapon("Lama Maledetta", 16, -32, "Potere immenso al prezzo della precisione.")
    );

    private static final List<Armor> ARMATURE = List.of(
            new Armor("Giaco di Cuoio", 15, 0.08, "Leggero e versatile."),
            new Armor("Cotta di Maglia", 30, 0.0, "Solida protezione di base."),
            new Armor("Armatura a Piastre", 55, -0.15, "Una fortezza lenta da indossare."),
            new Armor("Scaglie d'Ombra", 5, 0.28, "Fragile, ma sfugge a quasi ogni colpo.")
    );

    public static Weapon armaCasuale(Random random) {
        return ARMI.get(random.nextInt(ARMI.size()));
    }

    public static Armor armaturaCasuale(Random random) {
        return ARMATURE.get(random.nextInt(ARMATURE.size()));
    }

    public static Equipaggiamento donoCasuale(Random random) {
        if (random.nextBoolean()) {
            return armaCasuale(random);
        }
        return armaturaCasuale(random);
    }

    public static Equipaggiamento donoCasualeDiverso(Random random, Equipaggiamento giaScelto) {
        Equipaggiamento candidato;
        do {
            candidato = donoCasuale(random);
        } while (giaScelto != null && candidato.getNome().equals(giaScelto.getNome()));
        return candidato;
    }
}