package molecules;

import io.PDB;
import io.PDBParser;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import moleview.Atom3DStickModel;
import moleview.AtomElement;
import rnaxd.RNAXDPresenter;
import utils.fx3d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by Jogi on 24.01.2016.
 */
public class RNASequence extends Group {
    private final ArrayList<Nucleotide> nucleotides = new ArrayList<>();
    private ArrayList<DrawModel> models = new ArrayList<>();
    private NucleotideSelectionModel<Nucleotide> selectionModel;

    public RNASequence(PDB pdb) {
        Point3D coordinates;
        int resSeq = -1;
        Nucleotide nucleotide = null;

        for (int i = 0; i < pdb.getAtoms().size(); i++) {
            if (pdb.getAtoms().get(i).getResSeq() != resSeq) {
                if (nucleotide != null && !nucleotide.checkValidity())
                    System.err.println("Validity Check error for PDB-Entry with resSeq: " + resSeq);
                if (resSeq != -1)
                    nucleotides.add(nucleotide);
                switch(pdb.getAtoms().get(i).getResName()) {
                    case "ADE":
                    case "A":
                        nucleotide = new Adenine();
                        break;
                    case "URA":
                    case "U":
                        nucleotide = new Uracil();
                        break;
                    case "GUA":
                    case "G":
                        nucleotide = new Guanine();
                        break;
                    case "CYT":
                    case "C":
                        nucleotide = new Cytosine();
                        break;
                    default:
                        System.err.println("Nucleotide Base Type >" + pdb.getAtoms().get(i).getResName() + "< is unknown. Check your pdb-file");
                        continue;
                }
                resSeq = pdb.getAtoms().get(i).getResSeq();
            }
            System.err.println(pdb.getAtoms().get(i).getName());
            System.err.println(pdb.getAtoms().get(i).getPoint3D());
            nucleotide.set(pdb.getAtoms().get(i).getName(), pdb.getAtoms().get(i).getPoint3D());
        }
        if (nucleotide != null && !nucleotide.checkValidity())
            System.err.println("Validity Check error for PDB-Entry with resSeq: " + resSeq);
        nucleotides.add(nucleotide);
    }

    public void addModel(DrawModel model) {
        models.add(model);
        Nucleotide[] nucs = new Nucleotide[getNucleotides().size()];
        for (int i = 0; i < getNucleotides().size(); i++) {
            nucs[i] = getNucleotides().get(i);
        }
        model.draw(nucs);
    }

    public Point3D computeCenter() {
        Point3D p = new Point3D(0,0,0);
        for (Nucleotide n : nucleotides) {
            p = p.add(utils.fx3d.computeCenter(n.getNucleotideCoordinates()));
            p = p.add(utils.fx3d.computeCenter(n.getBaseCoordinates()));
        }

        p = p.multiply(1/((double) nucleotides.size() * 2));
        return p;
    }

    public int[][] computeWCBonds() {
        return computeWCBonds(true);
    }

    public int[][] computeWCBonds(boolean withSequence) {
        ArrayList<int[]> edges = new ArrayList<>();

        if (withSequence) {
            for (int i = 0; i < nucleotides.size() - 1; i++)
                edges.add(new int[]{i, i + 1});
        }

        for (int i = 0 ; i < nucleotides.size(); i++) {
            for (int j = i+1 ; j < nucleotides.size(); j++) {
                Point3D cA = nucleotides.get(i).get("C2");
                Point3D cB = nucleotides.get(j).get("C2");
                if (cA == null || cB == null) {
                    System.err.println("cA or cB is null");
                    if (Nucleotide.isWatsonCrick(nucleotides.get(i), nucleotides.get(j))) {
                        int[] edge = {i, j};
                        edges.add(edge);
                    }
                }
                else if (cA.distance(cB) < 6.0 /*&& (j-i) != 1*/) {
                    System.err.println("########" + i + "-" + j + "########");
                    System.err.println("Distance C2: " + cA.distance(cB));
                    if (Nucleotide.isWatsonCrick(nucleotides.get(i), nucleotides.get(j))) {
                        int[] edge = {i, j};
                        edges.add(edge);
                    }
                }
            }
        }
        return edges.toArray(new int[edges.size()][2]);
    }

    public void setSelectionModel(NucleotideSelectionModel<Nucleotide> selectionModel) {
        this.selectionModel = selectionModel;
        setSelectionCapture();
    }

    private void setSelectionCapture() {
        for (DrawModel model : models) {
            setSelectionCapture(model);
        }
        listenToIndices();
    }

    private void listenToIndices() {
        selectionModel.getSelectedIndices().addListener((ListChangeListener<Integer>) c -> {
            for (DrawModel model : getModels()) {
                int[] array = new int[c.getList().size()];
                for (int i = 0; i < c.getList().size(); i++)
                    array[i] = c.getList().get(i);
                model.exposeNucleotide(array);
            }
        });
    }

    private void setSelectionCapture(DrawModel drawModel) {
        for (int i = 0; i < drawModel.getResidues().size(); i++) {
            final int index = i;
            drawModel.getResidues().get(index).setOnMouseClicked((me) -> {
                if (me.isControlDown()) {
                    System.out.println("control is down");
                    if (selectionModel.isSelected(index))
                        selectionModel.clearSelection(index);
                    else
                        selectionModel.select(index);
                } else if (selectionModel.isSelected(index)) {
                    selectionModel.clearSelection(index);
                } else {
                    selectionModel.clearSelection();
                    selectionModel.select(index);
                }
            });
        }
    }

    public String extractString() {
        StringBuilder b = new StringBuilder();
        for (Nucleotide n : nucleotides) {
            if (n instanceof Adenine)
                b.append("A");
            if (n instanceof Guanine)
                b.append("G");
            if (n instanceof  Cytosine)
                b.append("C");
            if (n instanceof Uracil)
                b.append("U");
        }
        return b.toString();
    }

    public NucleotideSelectionModel<Nucleotide> getSelectionModel() {
        return selectionModel;
    }

    public ArrayList<DrawModel> getModels() {
        return models;
    }

    public ArrayList<Nucleotide> getNucleotides() {
        return nucleotides;
    }
}
