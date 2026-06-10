package it.unicam.cs.mpgc.rpg125939.mpgcrpg.controller;

import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.FormaGeometrica;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.GameState;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.MonsterIntent;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.enums.PlayerAction;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Monster;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.Player;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters.Zombie;
import it.unicam.cs.mpgc.rpg125939.mpgcrpg.model.monsters.Soldier;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.List;

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
    @FXML private Button btnNormale;
    @FXML private Button btnPesante;
    @FXML private Button btnSchivata;
    @FXML private Button btnPozione;
    @FXML private Button btnContinua;

    private GameManager gameManager;

    @FXML
    public void initialize() {
        Player player = new Player();
        this.gameManager = new GameManager(player);
        this.gameManager.addPropertyChangeListener(this);

        List<Monster> scontroDemo = new ArrayList<>();
        scontroDemo.add(new Zombie());
        scontroDemo.add(new Soldier());
        gameManager.iniziaCombattimento(scontroDemo);
        gameManager.preparaTurno();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        aggiornaVista();
    }

    @FXML
    private void onAttaccoNormale() {
        azioneGiocatore(PlayerAction.ATTACCO_NORMALE);
    }

    @FXML
    private void onAttaccoPesante() {
        azioneGiocatore(PlayerAction.ATTACCO_PESANTE);
    }

    @FXML
    private void onSchivata() {
        azioneGiocatore(PlayerAction.SCHIVATA);
    }

    @FXML
    private void onPozione() {
        azioneGiocatore(PlayerAction.POZIONE);
    }

    @FXML
    private void onContinua() {
        // Avanzamento dialoghi NPC: cablato nella pipeline delle stanze (STEP 7).
    }

    private void azioneGiocatore(PlayerAction azione) {
        gameManager.eseguiTurno(azione);
        if (gameManager.getStato() == GameState.COMBAT && !gameManager.isScontroConcluso()) {
            gameManager.preparaTurno();
        }
    }

    private void aggiornaVista() {
        Player p = gameManager.getPlayer();
        int hpMax = p.getHpMaxReali();
        double rapporto = hpMax == 0 ? 0.0 : (double) p.getHp() / hpMax;
        hpBar.setProgress(Math.max(0.0, rapporto));
        lblHp.setText(p.getHp() + "/" + hpMax);
        lblForza.setText("FOR: " + p.getForLevel());
        lblFortuna.setText("LUK: " + p.getLukLevel());
        lblFrammenti.setText("Frammenti: " + p.getFragments());
        lblPozioni.setText("Pozioni: " + p.getPotionCount());

        aggiornaArena();
        aggiornaLog();
        aggiornaPulsanti();
    }

    private void aggiornaArena() {
        arenaBox.getChildren().clear();
        arenaBox.getChildren().add(creaNodoGiocatore());

        Region separatore = new Region();
        HBox.setHgrow(separatore, Priority.ALWAYS);
        arenaBox.getChildren().add(separatore);

        HBox gruppoMostri = new HBox(30.0);
        gruppoMostri.setAlignment(Pos.CENTER);
        for (Monster m : gameManager.getMostriCorrenti()) {
            gruppoMostri.getChildren().add(creaNodoMostro(m));
        }
        arenaBox.getChildren().add(gruppoMostri);
    }

    private Node creaNodoGiocatore() {
        Player p = gameManager.getPlayer();
        Circle corpo = new Circle(40.0, Color.web("#2E86C1"));
        Label nome = new Label("Eroe");
        Label hp = new Label(p.getHp() + " HP");
        VBox box = new VBox(6.0, corpo, nome, hp);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node creaNodoMostro(Monster m) {
        Shape forma = creaForma(m);
        if (m == gameManager.getBersaglio()) {
            forma.setStroke(Color.GOLD);
            forma.setStrokeWidth(4.0);
        }

        Label nome = new Label(m.getNome());
        Label hp = new Label(m.getHp() + "/" + m.getMaxHp() + " HP");

        MonsterIntent intento = gameManager.getIntenti().get(m);
        Label etichettaIntento = new Label(intento == null ? "-" : intento.toString());

        VBox box = new VBox(6.0, forma, nome, hp, etichettaIntento);
        box.setAlignment(Pos.CENTER);
        box.setOnMouseClicked(e -> gameManager.selezionaBersaglio(m));
        return box;
    }

    private Shape creaForma(Monster m) {
        Color colore = Color.web(m.getColoreEsadecimale());
        FormaGeometrica tipo = m.getFormaGeometrica();
        switch (tipo) {
            case CERCHIO:
                return new Circle(38.0, colore);
            case POLIGONO:
                Polygon poligono = new Polygon(0.0, -50.0, 50.0, 40.0, -50.0, 40.0);
                poligono.setFill(colore);
                return poligono;
            case RETTANGOLO:
            default:
                return new Rectangle(72.0, 92.0, colore);
        }
    }

    private void aggiornaLog() {
        logArea.setText(String.join("\n", gameManager.getLog()));
        logArea.setScrollTop(Double.MAX_VALUE);
        logArea.positionCaret(logArea.getLength());
    }

    private void aggiornaPulsanti() {
        GameState stato = gameManager.getStato();
        boolean inCombattimento = (stato == GameState.COMBAT);
        boolean inDialogo = (stato == GameState.DIALOGUE);

        btnNormale.setDisable(!inCombattimento);
        btnPesante.setDisable(!inCombattimento);
        btnSchivata.setDisable(!inCombattimento);
        btnPozione.setDisable(!inCombattimento);
        btnContinua.setDisable(!inDialogo);
    }
}