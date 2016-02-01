package molecules;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by Jogi on 25.01.2016.
 */
public class Residue extends Group {
    private Point3D first;
    private Point3D second;

    protected boolean selected = false;

    public void setFirst(Point3D anchorpoint) {
        this.first = anchorpoint;
    }
    public void setSecond(Point3D anchorpoint) {
        this.second = anchorpoint;
    }

    public Point3D getFirst() {
        return first;
    }

    public Point3D getSecond() {
        return second;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {}

    public void deselect() {}
}