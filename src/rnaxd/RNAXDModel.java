package rnaxd;

import io.PDB;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.File;

/**
 * Created by Jogi on 20.01.2016.
 */
public class RNAXDModel {
    private String fileName;
    private final BooleanProperty FILE_LOADED = new SimpleBooleanProperty(false);
    private PDB pdbfile;
    private String sequence;
    private int[][] bonds;

    public boolean getFILE_LOADED() {
        return FILE_LOADED.get();
    }

    public void setFileLoaded(boolean isloaded) {
        FILE_LOADED.set(isloaded);
    }

    public ReadOnlyBooleanProperty fileLoadedProperty() {
        return FILE_LOADED;
    }

    public PDB getPdbfile() {
        return pdbfile;
    }

    public void setPdbfile(PDB pdbfile) {
        this.pdbfile = pdbfile;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSequence() {
        return sequence;
    }

    public void fileReloaded() {
        FILE_LOADED.set(false);
        FILE_LOADED.set(true);
    }

    public int[][] getBonds() {
        return bonds;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setBonds(int[][] bonds) {
        this.bonds = bonds;
    }
}
