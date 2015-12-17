package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Joachim on 09/12/2015.
 */
public class PDB implements Iterable<PDBAtom> {
    private ArrayList<PDBAtom> atoms = new ArrayList<>();
    private ArrayList<PDBConect> conect = new ArrayList<>();

    public ArrayList<PDBAtom> getAtoms() {
        return atoms;
    }

    public ArrayList<PDBConect> getConect() {
        return conect;
    }

    @Override
    public Iterator<PDBAtom> iterator() {
        return atoms.iterator();
    }

    public static void main(String[] args) throws IOException {
        File pdbFile = new File("pdb1aa5.ent");
        PDB pdb = PDBParser.parse(pdbFile);
        for (PDBAtom atom : pdb.getAtoms()) {
            System.out.println(atom.getElement());
        }
        for (PDBConect conect : pdb.getConect()) {
            System.out.println(conect.getSerialAtom());
        }
    }
}
