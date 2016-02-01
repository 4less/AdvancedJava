
package molecules;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jogi on 24.01.2016.
 */
public abstract class DrawModel extends Group {
    public final ArrayList<Residue> residues = new ArrayList<>();
    public abstract void draw(Nucleotide... nucleotides);
    public ArrayList<Residue> getResidues() {
        return residues;
    }
    private Group selectedItems = new Group();
    private static final Color coatingColor = Color.rgb(255,0,0,0.3);
    {this.getChildren().add(selectedItems);}

    public void exposeNucleotide(int... index) {
        Arrays.sort(index);
        selectedItems.getChildren().clear();
        if (index.length == 0) {
            return;
        } else {
            for (int i = 0; i < index.length; i++) {
                Residue res = residues.get(index[i]);
                selectedItems.getChildren().add(utils.fx3d.coatGroup3D(res, coatingColor, 2.0));
            }
        }
    }
}