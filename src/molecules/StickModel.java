package molecules;


import javafx.geometry.Point3D;
import moleview.Atom3DStickModel;
import moleview.AtomElement;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Jogi on 25.01.2016.
 */
public class StickModel extends DrawModel {
    private String first = "O3'";
    private String second = "P";

    public void draw(Nucleotide... nucleotides) {
        Residue res = null;
        for (int i = 0; i < nucleotides.length; i++) {
            res = new Residue();
            res.setFirst(nucleotides[i].get("O3'"));
            res.setSecond(nucleotides[i].get("P"));
            for (String nucleotide : nucleotides[i].getAtoms()) {
                Point3D center = nucleotides[i].get(nucleotide);
                if (center == null) {
                    System.err.println(i + " : " + nucleotide + " : " + center);
                    continue;
                }
                Atom3DStickModel atom = new Atom3DStickModel(AtomElement.createElement(nucleotide.substring(0,1)), center);
                if (nucleotides[i].bondedAtoms(nucleotide) == null) {
                    System.out.println(i + " : " + nucleotide);
                    System.exit(0);
                }
                for (String bonds : nucleotides[i].bondedAtoms(nucleotide)) {
                    Point3D point = nucleotides[i].get(bonds);
                    if (point == null) {
                        System.err.println("pos: " + i + "; Connection: " + nucleotide + " -- " + bonds + " cannot be created. \n -> " + bonds + " might not available (missing in pdb-file) \n -> " + bonds + " might be an unknown atom name");
                        continue;
                    }
                    if (point == null || center == null || atom == null) {
                        System.out.println(center + " -- " + point);
                        System.out.println(i + " : " + nucleotide + " -- " + bonds);
                        System.exit(0);
                    }
                    atom.addEdge(center, point);
                }
                res.setFirst(nucleotides[i].get(first));
                res.setSecond(nucleotides[i].get(second));
                res.getChildren().addAll(atom.getNodes());
            }
            residues.add(res);
        }
        addBackBone();
    }

    public void addBackBone() {
        if (residues.size() > 1) {
            this.getChildren().add(residues.get(0));

            Residue last = residues.get(0);
            Residue current;
            Atom3DStickModel a;
            Atom3DStickModel b;
            Point3D aPoint;
            Point3D bPoint;
            for (int i = 1; i < residues.size(); i++) {
                this.getChildren().add(residues.get(i));
                current = residues.get(i);

                aPoint = last.getFirst();
                bPoint = current.getSecond();

                if (aPoint != null && bPoint != null && aPoint.distance(bPoint) < 7) {

                    a = new Atom3DStickModel(AtomElement.createElement(first.substring(0, 1)), aPoint, bPoint.midpoint(aPoint));
                    b = new Atom3DStickModel(AtomElement.createElement(second.substring(0, 1)), bPoint, aPoint.midpoint(bPoint));
                    this.getChildren().addAll(a.getNodes());
                    this.getChildren().addAll(b.getNodes());
                }
                last = current;
            }
        }
    }
}
