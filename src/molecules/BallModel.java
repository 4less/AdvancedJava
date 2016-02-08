package molecules;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import moleview.AtomElement;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Jogi on 27.01.2016.
 */
public class BallModel extends DrawModel {
    private static final HashMap<String, Double> radiiMap = new HashMap<>();
    {
        radiiMap.put("N", 0.7);
        radiiMap.put("O", 0.66);
        radiiMap.put("H", 0.37);
        radiiMap.put("P", 1.1);
        radiiMap.put("C", 0.77);
    }

    public static final Color UNSELECTED_COLOR = Color.rgb(0,0,255, 0.2);

    @Override
    public void draw(Nucleotide... nucleotides) {
        Residue res;
        for (int i = 0; i < nucleotides.length; i++) {
            res = new BallResidue();

            for (String nucleotide : nucleotides[i].getAtoms()) {
                if (nucleotides[i].get(nucleotide) != null) {
                    res.getChildren().add(createSphere(nucleotides[i].get(nucleotide), nucleotide.substring(0,1)));
                    ((BallResidue) res).addName(nucleotide.substring(0,1));
                }
            }
            residues.add(res);
            this.getChildren().add(res);
        }
    }

    private Sphere createSphere(Point3D point, String atom) {
        Sphere result = new Sphere();
        result.setRadius(radiiMap.get(atom));
        result.setTranslateX(point.getX());
        result.setTranslateY(point.getY());
        result.setTranslateZ(point.getZ());
        result.setDrawMode(DrawMode.FILL);
        result.setMaterial(new PhongMaterial(AtomElement.createElement(atom).getColor()));
        return result;
    }

    public void exposeNucleotide(int... index) {
        Arrays.sort(index);
        if (index.length == 0) {
            residues.forEach(Residue::deselect);
        } else {
            for (int i = 0, j = 0; i < residues.size(); i++) {
                BallResidue res = (BallResidue) residues.get(i);
                if (i == index[j]) {
                    if (j < index.length - 1)
                        j++;
                    if (res.isSelected())
                        res.deselect();
                    continue;
                }
                res.select();
            }
        }
    }
}
