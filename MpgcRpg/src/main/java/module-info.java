module it.unicam.cs.mpgc.rpg125939.mpgcrpg {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens it.unicam.cs.mpgc.rpg125939.mpgcrpg to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg125939.mpgcrpg.controller to javafx.fxml;

    exports it.unicam.cs.mpgc.rpg125939.mpgcrpg;
}