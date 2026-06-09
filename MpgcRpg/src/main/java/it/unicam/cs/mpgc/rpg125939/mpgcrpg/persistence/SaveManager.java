package it.unicam.cs.mpgc.rpg125939.mpgcrpg.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManager {

    private static final String SAVE_FILE = "savegame.dat";

    public boolean esisteSalvataggio() {
        return new File(SAVE_FILE).exists();
    }

    public void salva(GameSnapshot snapshot) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(snapshot);
        }
    }

    public GameSnapshot carica() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (GameSnapshot) in.readObject();
        }
    }

    public boolean cancellaSalvataggio() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}