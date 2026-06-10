package it.unicam.cs.mpgc.rpg125939.mpgcrpg.controller;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.GameState;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.PlayerAction;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class GameManager {

    private static final int MOLTIPLICATORE_PESANTE = 2;
    private static final double SCHIVATA_BASE = 0.50;
    private static final double FATTORE_POZIONE = 0.20;
    private static final int CURA_MOSTRO = 15;

    private final Player player;
    private final Random random = new Random();

    private final List<Monster> mostriCorrenti = new ArrayList<>();
    private final Map<Monster, MonsterIntent> intenti = new LinkedHashMap<>();
    private final List<String> log = new ArrayList<>();
    public static final String PROP_AGGIORNAMENTO = "aggiornamentoModello";
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Monster bersaglio;
    private GameState stato = GameState.MENU;

    public GameManager(Player player) {
        this.player = player;
    }

    public void iniziaCombattimento(List<Monster> mostri) {
        mostriCorrenti.clear();
        intenti.clear();
        mostriCorrenti.addAll(mostri);
        bersaglio = mostriCorrenti.isEmpty() ? null : mostriCorrenti.get(0);
        stato = GameState.COMBAT;
        log.add("Inizia lo scontro.");
    }

    public void preparaTurno() {
        if (stato != GameState.COMBAT) {
            return;
        }
        intenti.clear();
        for (Monster m : mostriCorrenti) {
            if (m.isVivo()) {
                MonsterIntent intento = m.decidiMossa(player);
                intenti.put(m, intento);
                log.add(m.getNome() + " - intenzione: " + intento);
            }
        }
    }

    public void selezionaBersaglio(Monster m) {
        if (m != null && m.isVivo() && mostriCorrenti.contains(m)) {
            this.bersaglio = m;
        }
    }

    public void eseguiTurno(PlayerAction azione) {
        if (stato != GameState.COMBAT) {
            return;
        }

        boolean haSchivato = false;
        switch (azione) {
            case ATTACCO_NORMALE:
                risolviAttaccoNormale();
                break;
            case ATTACCO_PESANTE:
                risolviAttaccoPesante();
                break;
            case SCHIVATA:
                haSchivato = true;
                log.add("Ti metti in guardia, pronto a deviare il colpo.");
                break;
            case POZIONE:
                risolviPozione();
                break;
        }

        rimuoviMostriSconfitti();

        if (mostriCorrenti.isEmpty()) {
            log.add("Tutti i nemici sono stati sconfitti.");
            notifica();
            return;
        }

        risolviRappresaglie(haSchivato);

        if (player.getHp() <= 0) {
            stato = GameState.GAME_OVER;
            log.add("Sei caduto. Il dungeon reclama un'altra vittima.");
        }

        notifica();
    }

    private void risolviAttaccoNormale() {
        if (bersaglio == null || !bersaglio.isVivo()) {
            return;
        }
        MonsterIntent intento = intenti.get(bersaglio);
        int danno = bersaglio.calcolaDannoRicevuto(player.getDannoTotale(), intento);
        bersaglio.setHp(bersaglio.getHp() - danno);
        log.add("Colpisci " + bersaglio.getNome() + " con un attacco normale: " + danno + " danni.");
    }

    private void risolviAttaccoPesante() {
        if (bersaglio == null || !bersaglio.isVivo()) {
            return;
        }
        MonsterIntent intento = intenti.get(bersaglio);
        int precisione = bersaglio.forzaPrecisionePesante(player.getPrecisionePesante());
        if (intento == MonsterIntent.VULNERABILE) {
            precisione = 100;
        }

        if (random.nextInt(100) < precisione) {
            int dannoBase = player.getDannoTotale() * MOLTIPLICATORE_PESANTE;
            int danno = bersaglio.calcolaDannoRicevuto(dannoBase, intento);
            bersaglio.setHp(bersaglio.getHp() - danno);
            log.add("Attacco pesante a segno su " + bersaglio.getNome() + ": " + danno + " danni!");
        } else {
            log.add("Il tuo attacco pesante manca " + bersaglio.getNome() + "!");
            bersaglio.subisciSchivata();
        }
    }

    private void risolviPozione() {
        if (player.getPotionCount() <= 0) {
            log.add("Non hai pozioni disponibili.");
            return;
        }
        int cura = (int) Math.round(player.getHpMaxReali() * FATTORE_POZIONE);
        player.setHp(player.getHp() + cura);
        player.setPotionCount(player.getPotionCount() - 1);
        log.add("Bevi una pozione e recuperi " + cura + " HP.");
    }

    private void rimuoviMostriSconfitti() {
        List<Monster> caduti = new ArrayList<>();
        for (Monster m : mostriCorrenti) {
            if (!m.isVivo()) {
                caduti.add(m);
            }
        }
        for (Monster m : caduti) {
            player.setFragments(player.getFragments() + m.getFragmentReward());
            log.add(m.getNome() + " è sconfitto. Ottieni " + m.getFragmentReward() + " frammenti.");
            mostriCorrenti.remove(m);
            intenti.remove(m);
        }
        if (bersaglio != null && !bersaglio.isVivo()) {
            bersaglio = mostriCorrenti.isEmpty() ? null : mostriCorrenti.get(0);
        }
    }

    private void risolviRappresaglie(boolean haSchivato) {
        for (Monster m : mostriCorrenti) {
            if (!m.isVivo()) {
                continue;
            }
            MonsterIntent intento = intenti.get(m);

            if (intento == MonsterIntent.CURA) {
                m.setHp(m.getHp() + CURA_MOSTRO);
                log.add(m.getNome() + " incanala energia e si rigenera.");
                continue;
            }

            int dannoBase = dannoOffensivo(m, intento);
            if (dannoBase <= 0) {
                log.add(m.getNome() + " non sferra alcun attacco in questo turno.");
                continue;
            }

            int dannoFinale = dannoBase;
            boolean perforante = (intento == MonsterIntent.ATTACCO_AGILE);
            if (haSchivato && !perforante) {
                double fattore = Math.max(0.0, SCHIVATA_BASE - player.getArmor().getBonusDodge());
                dannoFinale = (int) Math.round(dannoBase * fattore);
            }

            player.setHp(player.getHp() - dannoFinale);
            log.add(m.getNome() + " ti colpisce per " + dannoFinale + " danni.");

            if (player.getHp() <= 0) {
                break;
            }
        }
    }

    private int dannoOffensivo(Monster m, MonsterIntent intento) {
        if (intento == null) {
            return 0;
        }
        switch (intento) {
            case ATTACCO_STANDARD:
                return m.getBaseDamage();
            case ATTACCO_PESANTE:
                return m.getBaseDamage() * MOLTIPLICATORE_PESANTE;
            case ATTACCO_AGILE:
                return m.getBaseDamage();
            default:
                return 0;
        }
    }



    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    private void notifica() {
        pcs.firePropertyChange(PROP_AGGIORNAMENTO, null, this);
    }

    public boolean isScontroConcluso() {
        return mostriCorrenti.isEmpty();
    }

    public boolean isGameOver() {
        return stato == GameState.GAME_OVER;
    }

    public GameState getStato() {
        return stato;
    }

    public Player getPlayer() {
        return player;
    }

    public Monster getBersaglio() {
        return bersaglio;
    }

    public List<Monster> getMostriCorrenti() {
        return new ArrayList<>(mostriCorrenti);
    }

    public Map<Monster, MonsterIntent> getIntenti() {
        return new LinkedHashMap<>(intenti);
    }

    public List<String> getLog() {
        return log;
    }
}