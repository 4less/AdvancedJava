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
                    case "A":
                        nucleotide = new Adenine();
                        break;
                    case "U":
                        nucleotide = new Uracil();
                        break;
                    case "G":
                        nucleotide = new Guanine();
                        break;
                    case "C":
                        nucleotide = new Cytosine();
                        break;
                }
                resSeq = pdb.getAtoms().get(i).getResSeq();
            }
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

    public static void main(String[] args) throws IOException {
        RNASequence seq = new RNASequence(PDBParser.parse(new File("AUGC.pdb")));
        System.out.println(seq.getNucleotides().size());
        System.out.println("H2: " + seq.getNucleotides().get(1).get("H2"));
        System.out.println("P: " + seq.getNucleotides().get(1).get("P"));
        System.out.println("C5: " + seq.getNucleotides().get(1).get("C5"));
    }
}
