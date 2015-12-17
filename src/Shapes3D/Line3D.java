package Shapes3D;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by Joachim on 09/12/2015.
 */
public class Line3D extends Cylinder {
    private Point3D start;
    private Point3D end;

    public Line3D(Point3D start, Point3D end, double radius) {
        super(radius, end.distance(start));
        this.start = start;
        this.end = end;
        setPosition();
    }

    private void setPosition() {
        Point3D mid = end.midpoint(start);
        Point3D diff = end.subtract(start);
        Point3D yAxis = new Point3D(0,1,0);
        Translate moveToMidPoint = new Translate(mid.getX(),mid.getY(),mid.getZ());
        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        this.getTransforms().addAll(moveToMidPoint,rotateAroundCenter);
    }
}
