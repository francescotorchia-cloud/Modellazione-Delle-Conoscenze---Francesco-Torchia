package it.unicam.cs.mpgc.rpg125939.mpgcrpg.controller;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.FormaGeometrica;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.GameState;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.PlayerAction;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.persistence.SaveManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainViewController implements PropertyChangeListener {

    @FXML private ProgressBar hpBar;
    @FXML private Label lblHp;
    @FXML private Label lblForza;
    @FXML private Label lblFortuna;
    @FXML private Label lblStanza;
    @FXML private Label lblFrammenti;
    @FXML private Label lblPozioni;
    @FXML private HBox arenaBox;
    @FXML private TextArea logArea;
    @FXML private HBox menuBox;
    @FXML private HBox combatBox;
    @FXML private HBox dialogoBox;
    @FXML private HBox offertaBox;
    @FXML private HBox santuarioBox;
    @FXML private HBox fineBox;
    @FXML private Button btnCaricaPartita;
    @FXML private Button btnVit;
    @FXML private Button btnFor;
    @FXML private Button btnLuk;

    private DungeonManager dungeon;
    private boolean easterEggMostrato;

    @FXML
    public void initialize() {
        this.dungeon = new DungeonManager(new SaveManager());
        this.dungeon.addPropertyChangeListener(this);
        logArea.setText("Tiny Dungeon\n\nScendi nelle profondità e affronta ciò che ti attende.\nInizia una Nuova Partita o riprendi dall'ultimo Santuario.");
        aggiornaVista();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        aggiornaVista();
    }

    @FXML
    private void onNuovaPartita() {
        easterEggMostrato = false;
        dungeon.nuovaPartita();
    }

    @FXML
    private void onCaricaPartita() {
        easterEggMostrato = false;
        dungeon.caricaPartita();
    }

    @FXML
    private void onAttaccoNormale() {
        dungeon.azioneCombattimento(PlayerAction.ATTACCO_NORMALE);
    }

    @FXML
    private void onAttaccoPesante() {
        dungeon.azioneCombattimento(PlayerAction.ATTACCO_PESANTE);
    }

    @FXML
    private void onSchivata() {
        dungeon.azioneCombattimento(PlayerAction.SCHIVATA);
    }

    @FXML
    private void onPozione() {
        dungeon.azioneCombattimento(PlayerAction.POZIONE);
    }

    @FXML
    private void onContinua() {
        dungeon.continua();
    }

    @FXML
    private void onEquipaggia() {
        dungeon.equipaggiaDono();
    }

    @FXML
    private void onRifiuta() {
        dungeon.rifiutaDono();
    }

    @FXML
    private void onPotenziaVitalita() {
        dungeon.potenziaVitalita();
    }

    @FXML
    private void onPotenziaForza() {
        dungeon.potenziaForza();
    }

    @FXML
    private void onPotenziaFortuna() {
        dungeon.potenziaFortuna();
    }

    @FXML
    private void onLasciaSantuario() {
        dungeon.lasciaSantuario();
    }

    private void aggiornaVista() {
        aggiornaTopBar();
        aggiornaArena();
        aggiornaLog();
        aggiornaPannelli();

        if (dungeon.isVittoria() && !easterEggMostrato) {
            easterEggMostrato = true;
            mostraEasterEgg();
        }
    }

    private void aggiornaTopBar() {
        Player p = dungeon.getPlayer();
        if (p == null) {
            hpBar.setProgress(0.0);
            lblHp.setText("-/-");
            lblForza.setText("FOR: -");
            lblFortuna.setText("LUK: -");
            lblStanza.setText("Stanza: -");
            lblFrammenti.setText("Frammenti: -");
            lblPozioni.setText("Pozioni: -");
            return;
        }
        int hpMax = p.getHpMaxReali();
        double rapporto = hpMax == 0 ? 0.0 : (double) p.getHp() / hpMax;
        hpBar.setProgress(Math.max(0.0, rapporto));
        lblHp.setText(p.getHp() + "/" + hpMax);
        lblForza.setText("FOR: " + p.getForLevel());
        lblFortuna.setText("LUK: " + p.getLukLevel());
        lblStanza.setText("Stanza: " + (dungeon.getProgress() == null ? "-" : dungeon.getProgress().getStanzaCorrente()));
        lblFrammenti.setText("Frammenti: " + p.getFragments());
        lblPozioni.setText("Pozioni: " + p.getPotionCount());
    }

    private void aggiornaArena() {
        arenaBox.getChildren().clear();
        GameState stato = dungeon.getStato();
        if (stato != GameState.COMBAT && stato != GameState.BOSS) {
            return;
        }

        arenaBox.getChildren().add(creaNodoGiocatore());

        Region separatore = new Region();
        HBox.setHgrow(separatore, Priority.ALWAYS);
        arenaBox.getChildren().add(separatore);

        HBox gruppoMostri = new HBox(30.0);
        gruppoMostri.setAlignment(Pos.CENTER);
        for (Monster m : dungeon.getMostriCorrenti()) {
            gruppoMostri.getChildren().add(creaNodoMostro(m));
        }
        arenaBox.getChildren().add(gruppoMostri);
    }

    private Node creaNodoGiocatore() {
        Player p = dungeon.getPlayer();
        Circle corpo = new Circle(40.0, Color.web("#2E86C1"));
        Label nome = new Label("Eroe");
        Label hp = new Label(p.getHp() + " HP");
        VBox box = new VBox(6.0, corpo, nome, hp);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node creaNodoMostro(Monster m) {
        Shape forma = creaForma(m);
        if (m == dungeon.getBersaglio()) {
            forma.setStroke(Color.GOLD);
            forma.setStrokeWidth(4.0);
        }

        Label nome = new Label(m.getNome());
        Label hp = new Label(m.getHp() + "/" + m.getMaxHp() + " HP");

        MonsterIntent intento = dungeon.getIntenti().get(m);
        Label etichettaIntento = new Label(intento == null ? "-" : intento.toString());

        VBox box = new VBox(6.0, forma, nome, hp, etichettaIntento);
        box.setAlignment(Pos.CENTER);
        box.setOnMouseClicked(e -> dungeon.selezionaBersaglio(m));
        return box;
    }

    private Shape creaForma(Monster m) {
        Color colore = Color.web(m.getColoreEsadecimale());
        FormaGeometrica tipo = m.getFormaGeometrica();
        switch (tipo) {
            case CERCHIO:
                return new Circle(38.0, colore);
            case POLIGONO:
                Polygon poligono = new Polygon(0.0, -55.0, 55.0, 45.0, -55.0, 45.0);
                poligono.setFill(colore);
                return poligono;
            case RETTANGOLO:
            default:
                return new Rectangle(72.0, 92.0, colore);
        }
    }

    private void aggiornaLog() {
        logArea.setText(String.join("\n", dungeon.getLog()));
        logArea.setScrollTop(Double.MAX_VALUE);
        logArea.positionCaret(logArea.getLength());
    }

    private void aggiornaPannelli() {
        GameState stato = dungeon.getStato();
        boolean inOfferta = dungeon.isInOffertaDono();

        mostraGruppo(menuBox, stato == GameState.MENU);
        mostraGruppo(combatBox, stato == GameState.COMBAT || stato == GameState.BOSS);
        mostraGruppo(dialogoBox, stato == GameState.DIALOGUE && !inOfferta);
        mostraGruppo(offertaBox, stato == GameState.DIALOGUE && inOfferta);
        mostraGruppo(santuarioBox, stato == GameState.CHECKPOINT);
        mostraGruppo(fineBox, stato == GameState.GAME_OVER || stato == GameState.VICTORY);

        if (stato == GameState.MENU) {
            btnCaricaPartita.setDisable(!dungeon.esisteSalvataggio());
        }

        if (stato == GameState.CHECKPOINT) {
            aggiornaPulsantiSantuario();
        }
    }

    private void aggiornaPulsantiSantuario() {
        Player p = dungeon.getPlayer();
        int costo = dungeon.getCostoPotenziamento();
        boolean abbastanza = p != null && p.getFragments() >= costo;

        btnVit.setText("Vitalità +1 (" + costo + ")");
        btnFor.setText("Forza +1 (" + costo + ")");
        btnLuk.setText("Fortuna +1 (" + costo + ")");

        btnVit.setDisable(!abbastanza);
        btnFor.setDisable(!abbastanza);
        btnLuk.setDisable(!abbastanza);
    }

    private void mostraGruppo(HBox gruppo, boolean visibile) {
        gruppo.setVisible(visibile);
        gruppo.setManaged(visibile);
    }

    private void mostraEasterEgg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tiny Dungeon — Epilogo");
        alert.setHeaderText("Hai sconfitto Fabrizio Fornari");
        alert.setContentText(
                "Ma la vera sfida era un'altra.\n\n"
                        + "Ti sei laureato!.");
        alert.showAndWait();
    }
}