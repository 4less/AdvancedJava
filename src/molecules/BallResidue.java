package molecules;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import moleview.AtomElement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jogi on 27.01.2016.
 */
public class BallResidue extends Residue {
    private ArrayList<String> atomNames = new ArrayList<>();

    public void addName(String name) {
        atomNames.add(name);
    }

    public void changeColor(Color color) {
        for (Node node : this.getChildren()) {
            ((PhongMaterial)((Sphere) node).getMaterial()).setDiffuseColor(color);
        }
    }

    public void select() {
        changeColor(BallModel.UNSELECTED_COLOR);
        super.selected = true;
    }

    public void deselect() {
        for (int i = 0; i < this.getChildren().size(); i++) {
            ((Sphere) this.getChildren().get(i)).setMaterial(new PhongMaterial(AtomElement.createElement(atomNames.get(i)).getColor()));
        }
        selected = false;
    }
}
