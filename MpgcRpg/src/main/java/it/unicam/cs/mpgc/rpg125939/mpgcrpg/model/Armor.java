package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

public class Armor implements Equipaggiamento {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int bonusHp;
    private final double bonusDodge;
    private final String descrizione;

    public Armor(String nome, int bonusHp, double bonusDodge, String descrizione) {
        this.nome = nome;
        this.bonusHp = bonusHp;
        this.bonusDodge = bonusDodge;
        this.descrizione = descrizione;
    }

    public int getBonusHp() {
        return bonusHp;
    }

    public double getBonusDodge() {
        return bonusDodge;
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
        return nome + " — HP " + formatta(bonusHp) + ", Schivata " + formattaDodge(bonusDodge) + ". " + descrizione;
    }

    @Override
    public void equipaggia(Player player) {
        player.equipaggiaArmatura(this);
    }

    @Override
    public Equipaggiamento equipaggiatoAttuale(Player player) {
        return player.getArmor();
    }

    private String formatta(int valore) {
        return valore >= 0 ? "+" + valore : String.valueOf(valore);
    }

    private String formattaDodge(double valore) {
        int percentuale = (int) Math.round(valore * 100);
        return percentuale >= 0 ? "+" + percentuale + "%" : percentuale + "%";
    }
}