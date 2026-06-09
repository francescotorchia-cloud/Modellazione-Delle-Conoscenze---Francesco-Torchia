package it.unicam.cs.mpgc.rpg125939.mpgcrpg.model;

import java.io.Serializable;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CAP_PRECISIONE = 95;

    private final int maxHp;
    private final int baseDamage;
    private final int baseLuck;

    private int hp;
    private int fragments;
    private int potionCount;

    private int vitLevel;
    private int forLevel;
    private int lukLevel;

    private Weapon weapon;
    private Armor armor;

    public Player() {
        this.maxHp = 100;
        this.baseDamage = 10;
        this.baseLuck = 40;

        this.hp = this.maxHp;
        this.fragments = 0;
        this.potionCount = 2;

        this.vitLevel = 0;
        this.forLevel = 0;
        this.lukLevel = 0;

        this.weapon = new Weapon("Spada di Legno Spuntata", 0, 0);
        this.armor = new Armor("Vestiti Logori", 0, 0);
    }

    public int getDannoTotale() {
        return baseDamage + (forLevel * 2) + weapon.getBonusDamage();
    }

    public int getHpMaxReali() {
        return maxHp + (vitLevel * 10) + armor.getBonusHp();
    }

    public int getPrecisionePesante() {
        int precisione = baseLuck + (lukLevel * 2) + weapon.getBonusLuck();
        return Math.min(precisione, CAP_PRECISIONE);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getBaseLuck() {
        return baseLuck;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, getHpMaxReali()));
    }

    public int getFragments() {
        return fragments;
    }

    public void setFragments(int fragments) {
        this.fragments = fragments;
    }

    public int getPotionCount() {
        return potionCount;
    }

    public void setPotionCount(int potionCount) {
        this.potionCount = potionCount;
    }

    public int getVitLevel() {
        return vitLevel;
    }

    public void setVitLevel(int vitLevel) {
        this.vitLevel = vitLevel;
    }

    public int getForLevel() {
        return forLevel;
    }

    public void setForLevel(int forLevel) {
        this.forLevel = forLevel;
    }

    public int getLukLevel() {
        return lukLevel;
    }

    public void setLukLevel(int lukLevel) {
        this.lukLevel = lukLevel;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }
}