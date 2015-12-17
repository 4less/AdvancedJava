package moleview;

import io.PDB;
import io.PDBParser;
import javafx.geometry.Point3D;
import javafx.scene.Group;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Joachim on 09/12/2015.
 */
public class Molecule3D extends Group {
    private class Bond {
        int a1,a2;

        public Bond(int a1, int a2) {
        this.a1 = a1;
        this.a2 = a2;
    }}

    private Point3D[] coordinates;
    private AtomElement[] elements;
    private ArrayList<Bond> bonds = new ArrayList<>();
    private Atom3DStickModel[] atoms;

    public Molecule3D(PDB pdb) {
        atoms = new Atom3DStickModel[pdb.getAtoms().size()];
        elements = new AtomElement[pdb.getAtoms().size()];
        coordinates = new Point3D[pdb.getAtoms().size()];
        for (int i = 0; i < pdb.getAtoms().size(); i++) {
            coordinates[i] = new Point3D(
                    pdb.getAtoms().get(i).getX(),
                    pdb.getAtoms().get(i).getY(),
                    pdb.getAtoms().get(i).getZ());
            elements[i] = AtomElement.createElement(pdb.getAtoms().get(i).getElement());

        }
    }

    public void scale(double factor) {
        for (Point3D point : coordinates) {
            point.multiply(factor);
        }
    }

    public void center() {
        Point3D median = new Point3D(0,0,0);

        for (Point3D atom : coordinates) {
            median = median.add(atom);
        }
        median = median.multiply(1.0/coordinates.length);
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = coordinates[i].subtract(median);
        }
        System.out.println(median.getX());
    }

    public void guessBonds(double angstrom) {
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates.length; j++) {
                Point3D atom1 = coordinates[i];
                Point3D atom2 = coordinates[j];
                if (coordinates[i] != atom2) {
                    if (atom1.distance(atom2) <= elements[i].dist(elements[j])) {
                        bonds.add(new Bond(i,j));
//                        System.out.println(elements[i] + " : " + atom1.distance(atom2) + " : " + elements[j]);
                    }
                }
            }
        }
    }

    public void build3DModel(double angstrom) {
        guessBonds(angstrom);
//        scale(10);
//        center();

        for (int i = 0; i < coordinates.length; i++)
            atoms[i] = new Atom3DStickModel(elements[i], coordinates[i]);
        for (Bond bond : bonds)
            atoms[bond.a1].addEdge(coordinates[bond.a1], coordinates[bond.a2]);
        for (Atom3DStickModel atom : atoms)
            this.getChildren().addAll(atom.getNodes());
    }

    public ArrayList<Bond> getBonds() {
        return bonds;
    }

    public AtomElement[] getElements() {
        return elements;
    }

    public static void main(String[] args) throws IOException {
        Molecule3D test = new Molecule3D(PDBParser.parse(new File("D:/test.pdb")));
        test.guessBonds(1.0);
        for (Bond bond : test.getBonds()) {
            System.out.println(test.getElements()[bond.a1] + " : " + bond.a1 + " : " + bond.a2 + " : " + test.getElements()[bond.a2]);
        }
        test.build3DModel(1.0);
    }
}
