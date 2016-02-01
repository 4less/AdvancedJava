package moleview;

import Shapes3D.Line3D;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

/**
 * Created by Joachim on 09/12/2015.
 */
public class Atom3DStickModel {
    private ArrayList<Line3D> lines = new ArrayList<>();
    private Sphere joint;
    private AtomElement atom;
    private double radius = 0.1;

    public Atom3DStickModel(AtomElement atom, Point3D origin, Point3D ... midpoints) {
        this.atom = atom;
        for (Point3D connector : midpoints)
            lines.add(new Line3D(origin, connector, radius));
        addJoint(origin);
        joint = addJoint(origin);
        initMaterial();
    }

    public void addEdge(Point3D origin, Point3D target) {
        Line3D line = new Line3D(origin, origin.midpoint(target), radius);
        lines.add(line);
        PhongMaterial material = new PhongMaterial(atom.getColor());
        line.setDrawMode(DrawMode.FILL);
        line.setMaterial(material);
    }

    private void initMaterial() {
        PhongMaterial material = new PhongMaterial(atom.getColor());
        for (Line3D line : lines) {
            line.setDrawMode(DrawMode.FILL);
            line.setMaterial(material);
        }
        joint.setDrawMode(DrawMode.FILL);
        joint.setMaterial(material);

    }

    private Sphere addJoint(Point3D capCord) {
        Sphere joint = new Sphere(radius);
        joint.setTranslateX(capCord.getX());
        joint.setTranslateY(capCord.getY());
        joint.setTranslateZ(capCord.getZ());
        return joint;
    }

    public Node[] getNodes() {
        Node[] nodes;
        nodes = new Node[lines.size() + 1];
        for (int i = 0; i < lines.size(); i++)
            nodes[i] = lines.get(i);
        nodes[nodes.length - 1] = joint;

        return nodes;
    }
}
