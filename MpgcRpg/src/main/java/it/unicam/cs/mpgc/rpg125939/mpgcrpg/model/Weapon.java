package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

public class Weapon implements Equipaggiamento {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int bonusDamage;
    private final int bonusLuck;
    private final String descrizione;

    public Weapon(String nome, int bonusDamage, int bonusLuck, String descrizione) {
        this.nome = nome;
        this.bonusDamage = bonusDamage;
        this.bonusLuck = bonusLuck;
        this.descrizione = descrizione;
    }

    public int getBonusDamage() {
        return bonusDamage;
    }

    public int getBonusLuck() {
        return bonusLuck;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String riepilogoStat() {
        return nome + " — Danno " + formatta(bonusDamage) + ", Fortuna " + formatta(bonusLuck) + ". " + descrizione;
    }

    @Override
    public void equipaggia(Player player) {
        player.setWeapon(this);
    }

    @Override
    public Equipaggiamento equipaggiatoAttuale(Player player) {
        return player.getWeapon();
    }

    private String formatta(int valore) {
        return valore >= 0 ? "+" + valore : String.valueOf(valore);
    }
}