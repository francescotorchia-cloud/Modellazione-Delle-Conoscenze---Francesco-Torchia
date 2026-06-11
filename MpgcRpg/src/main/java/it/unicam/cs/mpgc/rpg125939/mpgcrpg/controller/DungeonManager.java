package it.unicam.cs.mpgc.rpg125939.mpgcrpg.controller;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.GameState;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.PlayerAction;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.*;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters.Skeleton;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters.Soldier;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters.Troll;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters.Zombie;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.persistence.GameSnapshot;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.persistence.SaveManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DungeonManager {

    public static final String PROP_AGGIORNAMENTO = "aggiornamentoDungeon";

    private static final int COSTO_POTENZIAMENTO = 15;

    private final SaveManager saveManager;
    private final Random random = new Random();
    private final List<Room> stanze;
    private final List<String> log = new ArrayList<>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Player player;
    private DungeonProgress progress;
    private GameManager combatCorrente;
    private NPC npcCorrente;
    private GameState stato;
    private boolean inOffertaDono;

    public DungeonManager(SaveManager saveManager) {
        this.saveManager = saveManager;
        this.stanze = costruisciDungeon();
        this.stato = GameState.MENU;
    }

    public void nuovaPartita() {
        this.player = new Player();
        this.progress = new DungeonProgress();
        this.log.clear();
        this.log.add("Inizia la discesa nel Tiny Dungeon.");
        entraStanza(0);
    }

    public boolean esisteSalvataggio() {
        return saveManager.esisteSalvataggio();
    }

    public void caricaPartita() {
        try {
            GameSnapshot snapshot = saveManager.carica();
            this.player = snapshot.getPlayer();
            this.progress = snapshot.getProgress();
            this.log.clear();
            this.log.add("Partita ripristinata dall'ultimo Santuario.");
            entraStanza(progress.getStanzaCorrente());
        } catch (IOException | ClassNotFoundException e) {
            this.log.add("Impossibile caricare il salvataggio: file assente o corrotto.");
            notifica();
        }
    }

    private void entraStanza(int indice) {
        if (indice < 0 || indice >= stanze.size()) {
            return;
        }
        progress.setStanzaCorrente(indice);
        Room stanza = stanze.get(indice);
        this.combatCorrente = null;
        this.npcCorrente = null;
        this.inOffertaDono = false;

        switch (stanza.getTipo()) {
            case DIALOGO:
                npcCorrente = stanza.getNpc();
                npcCorrente.reset();
                stato = GameState.DIALOGUE;
                log.add("— " + npcCorrente.getNome() + " —");
                mostraProssimaBattuta();
                break;
            case COMBATTIMENTO:
                avviaCombattimento(stanza, GameState.COMBAT);
                break;
            case BOSS:
                avviaCombattimento(stanza, GameState.BOSS);
                break;
            case SANTUARIO:
                risolviSantuario(stanza);
                break;
            case VITTORIA:
                stato = GameState.VICTORY;
                log.add("Il dungeon è sconfitto. Sei sopravvissuto all'impossibile.");
                break;
        }
        notifica();
    }

    private void avviaCombattimento(Room stanza, GameState statoBattaglia) {
        combatCorrente = new GameManager(player);
        combatCorrente.iniziaCombattimento(stanza.generaMostri());
        combatCorrente.preparaTurno();
        stato = statoBattaglia;
    }

    private void risolviSantuario(Room stanza) {
        player.setHp(player.getHpMaxReali());
        player.setPotionCount(player.getPotionCount() + 1);
        stato = GameState.CHECKPOINT;
        log.add("Raggiungi il " + stanza.getNome() + ". Le tue ferite si rimarginano del tutto.");
        log.add("Trovi una pozione tra le offerte del Santuario.");
        try {
            saveManager.salva(new GameSnapshot(player, progress));
            log.add("Progressi incisi nella pietra del Santuario.");
        } catch (IOException e) {
            log.add("Attenzione: il rituale di salvataggio è fallito.");
        }
        log.add("Hai " + player.getFragments() + " frammenti. Spendili per rafforzarti.");
    }

    public void azioneCombattimento(PlayerAction azione) {
        if (combatCorrente == null || (stato != GameState.COMBAT && stato != GameState.BOSS)) {
            return;
        }
        combatCorrente.eseguiTurno(azione);

        if (combatCorrente.isGameOver()) {
            gestisciGameOver();
            return;
        }
        if (combatCorrente.isScontroConcluso()) {
            avanza();
            return;
        }
        combatCorrente.preparaTurno();
        notifica();
    }

    public void selezionaBersaglio(Monster m) {
        if (combatCorrente != null) {
            combatCorrente.selezionaBersaglio(m);
            notifica();
        }
    }

    public void continua() {
        if (stato != GameState.DIALOGUE || npcCorrente == null || inOffertaDono) {
            return;
        }
        if (npcCorrente.haAltreBattute()) {
            mostraProssimaBattuta();
            notifica();
            return;
        }
        if (npcCorrente.haDono()) {
            avviaOffertaDono();
            return;
        }
        avanza();
    }

    private void avviaOffertaDono() {
        inOffertaDono = true;
        Equipaggiamento dono = npcCorrente.getDono();
        log.add("— " + npcCorrente.getNome() + " ti porge qualcosa —");
        log.add("In offerta: " + dono.riepilogoStat());
        log.add("Equipaggiato ora: " + dono.equipaggiatoAttuale(player).riepilogoStat());
        notifica();
    }

    public void equipaggiaDono() {
        if (!inOffertaDono || npcCorrente == null || !npcCorrente.haDono()) {
            return;
        }
        Equipaggiamento dono = npcCorrente.getDono();
        dono.equipaggia(player);
        log.add("Hai equipaggiato: " + dono.getNome() + ".");
        inOffertaDono = false;
        avanza();
    }

    public void rifiutaDono() {
        if (!inOffertaDono) {
            return;
        }
        log.add("Hai rifiutato il dono e conservato il tuo equipaggiamento attuale.");
        inOffertaDono = false;
        avanza();
    }

    public void lasciaSantuario() {
        if (stato != GameState.CHECKPOINT) {
            return;
        }
        avanza();
    }

    public void potenziaVitalita() {
        if (spendiPotenziamento()) {
            player.setVitLevel(player.getVitLevel() + 1);
            player.setHp(player.getHpMaxReali());
            log.add("Vitalità accresciuta: i tuoi HP massimi aumentano.");
            notifica();
        }
    }

    public void potenziaForza() {
        if (spendiPotenziamento()) {
            player.setForLevel(player.getForLevel() + 1);
            log.add("Forza accresciuta: i tuoi colpi diventano più letali.");
            notifica();
        }
    }

    public void potenziaFortuna() {
        if (spendiPotenziamento()) {
            player.setLukLevel(player.getLukLevel() + 1);
            log.add("Fortuna accresciuta: l'Attacco Pesante andrà più spesso a segno.");
            notifica();
        }
    }

    private boolean spendiPotenziamento() {
        if (stato != GameState.CHECKPOINT) {
            return false;
        }
        if (player.getFragments() < COSTO_POTENZIAMENTO) {
            log.add("Frammenti insufficienti per il potenziamento.");
            notifica();
            return false;
        }
        player.setFragments(player.getFragments() - COSTO_POTENZIAMENTO);
        return true;
    }

    private void gestisciGameOver() {
        stato = GameState.GAME_OVER;
        saveManager.cancellaSalvataggio();
        log.add("Sei caduto nel dungeon. La pietra del Santuario si è frantumata.");
        notifica();
    }

    private void avanza() {
        entraStanza(progress.getStanzaCorrente() + 1);
    }

    private void mostraProssimaBattuta() {
        String battuta = npcCorrente.prossimaBattuta();
        if (battuta != null) {
            log.add(battuta);
        }
    }

    private List<Room> costruisciDungeon() {
        List<Room> r = new ArrayList<>();
        Equipaggiamento donoCavaliere = CatalogoEquipaggiamento.donoCasuale(random);
        Equipaggiamento donoEsploratore = CatalogoEquipaggiamento.donoCasualeDiverso(random, donoCavaliere);
        r.add(Room.dialogo(creaSaggio()));
        r.add(Room.combattimento("Cripta d'Ingresso", () -> List.of(new Skeleton())));
        r.add(Room.combattimento("Ossario", () -> List.of(new Skeleton(), new Skeleton())));
        r.add(Room.santuario("Santuario I"));
        r.add(Room.combattimento("Galleria Putrida", () -> List.of(new Zombie())));
        r.add(Room.combattimento("Fossa Comune", () -> List.of(new Zombie(), new Skeleton())));
        r.add(Room.santuario("Santuario II"));
        r.add(Room.dialogo(creaCavaliere(donoCavaliere)));
        r.add(Room.combattimento("Corpo di Guardia", () -> List.of(new Soldier())));
        r.add(Room.combattimento("Sala d'Armi", () -> List.of(new Soldier(), new Zombie())));
        r.add(Room.santuario("Santuario III"));
        r.add(Room.combattimento("Posto di Blocco", () -> List.of(new Soldier(), new Soldier())));
        r.add(Room.combattimento("Antro del Bruto", () -> List.of(new Troll())));
        r.add(Room.santuario("Santuario IV"));
        r.add(Room.combattimento("Caverna Profonda", () -> List.of(new Troll(), new Soldier())));
        r.add(Room.combattimento("Nido Oscuro", () -> List.of(new Troll(), new Zombie())));
        r.add(Room.santuario("Santuario V"));
        r.add(Room.dialogo(creaEsploratore(donoEsploratore)));
        r.add(Room.combattimento("Vestibolo del Signore", () -> List.of(new Troll(), new Soldier(), new Zombie())));
        r.add(Room.combattimento("Soglia Finale", () -> List.of(new Troll(), new Troll())));
        r.add(Room.santuario("Santuario Finale"));
        r.add(Room.boss("Trono del Dungeon", () -> List.of(new FinalBoss())));
        r.add(Room.vittoria());
        return r;
    }

    private NPC creaSaggio() {
        return new NPC("Vecchio Saggio", List.of(
                "Benvenuto, viandante. In pochi varcano questa soglia, e ancor meno ne fanno ritorno.",
                "Il dungeon ti osserva. Ogni creatura tradisce le proprie intenzioni un istante prima di colpire.",
                "Quando vedi un nemico prepararsi, scegli l'azione adatta a ciò che sta per fare. Reagire alla cieca, qui, significa morire.",
                "Prendi questi stracci e questa lama spuntata. Non è molto, ma è un inizio. Porta con te anche due pozioni.",
                "Va', e che la tua mira non tremi mai."
        ));
    }

    private NPC creaCavaliere(Equipaggiamento dono) {
        return new NPC("Cavaliere Caduto", List.of(
                "Fermati... non ho più la forza per impugnare la spada, ma posso ancora metterti in guardia.",
                "Le guardie del dungeon sono troppo agili: i colpi pesanti le mancano quasi sempre.",
                "Sgretola la loro salute con attacchi rapidi e continui. È l'unico modo per averne ragione.",
                "Prima che tu vada, prendi questo. A me non serve più."
        ), dono);
    }

    private NPC creaEsploratore(Equipaggiamento dono) {
        return new NPC("Esploratore Terrorizzato", List.of(
                "Indietro! No... aspetta. Se sei giunto fin qui, devi sapere come affrontarlo.",
                "Il Signore del Dungeon è un mostro antico, e si rigenera.",
                "Quando evoca la sua barriera magica non provare a difenderti: sta canalizzando un incantesimo di guarigione.",
                "Colpiscilo con un Attacco Pesante per spezzare il rituale. È la sua unica, vera debolezza.",
                "Ho trovato questo nel mio cammino. Tienilo, io non andrò oltre."
        ), dono);
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

    public GameState getStato() {
        return stato;
    }

    public Player getPlayer() {
        return player;
    }

    public DungeonProgress getProgress() {
        return progress;
    }

    public NPC getNpcCorrente() {
        return npcCorrente;
    }

    public boolean isInOffertaDono() {
        return inOffertaDono;
    }

    public Equipaggiamento getDonoCorrente() {
        return npcCorrente != null ? npcCorrente.getDono() : null;
    }

    public int getCostoPotenziamento() {
        return COSTO_POTENZIAMENTO;
    }

    public boolean isGameOver() {
        return stato == GameState.GAME_OVER;
    }

    public boolean isVittoria() {
        return stato == GameState.VICTORY;
    }

    public List<Monster> getMostriCorrenti() {
        return combatCorrente != null ? combatCorrente.getMostriCorrenti() : new ArrayList<>();
    }

    public Map<Monster, MonsterIntent> getIntenti() {
        return combatCorrente != null ? combatCorrente.getIntenti() : new LinkedHashMap<>();
    }

    public Monster getBersaglio() {
        return combatCorrente != null ? combatCorrente.getBersaglio() : null;
    }

    public List<String> getLog() {
        if (stato == GameState.COMBAT || stato == GameState.BOSS) {
            return combatCorrente != null ? combatCorrente.getLog() : log;
        }
        return log;
    }
}