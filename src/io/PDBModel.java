package io;

import java.util.ArrayList;

/**
 * Created by Jogi on 28.01.2016.
 */
public class PDBModel {
    private int serial;
    private ArrayList<PDBAtom> atoms = new ArrayList<>();

    public PDBModel(int serial) {
        this.serial = serial;
    }

    public ArrayList<PDBAtom> getAtoms() {
        return atoms;
    }

    public int getSerial() {
        return serial;
    }
}
