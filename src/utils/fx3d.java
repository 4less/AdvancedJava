package utils;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Created by Jogi on 29.01.2016.
 */
public class fx3d {
    public static Point3D computeCenter(ArrayList<Point3D> coordinates) {
        double x = 0, y = 0, z = 0;
        for (Point3D point : coordinates) {
            x += point.getX();
            y += point.getY();
            z += point.getZ();
        }
        int length = coordinates.size();
        return new Point3D(x / length, y /length, z / length);
    }

    public static Point3D computeCenter(Point3D[] coordinates) {
        double x = 0.0, y = 0.0, z = 0.0;
        int length = coordinates.length;
        for (Point3D point : coordinates) {
            if (point == null) {
                length--;
                continue;
            }
            x += point.getX();
            y += point.getY();
            z += point.getZ();
        }
        Point3D center = new Point3D(x / (double) length, y / (double) length, z / (double) length);
        return center;
    }


    public static Point3D computeCenter(Point3D[] coordinates, Point3D[]... coordinatesArray) {
        System.err.print("computeCenter(coordinates): ");
        Point3D coordinateCenter = computeCenter(coordinates);
        System.err.println("coordinatearray length" + coordinatesArray.length + ";");
        if (coordinatesArray.length < 1)
            return coordinateCenter;
        double x = coordinateCenter.getX(), y = coordinateCenter.getY(), z = coordinateCenter.getZ();
        int length = coordinates.length;
        length++;
        for (Point3D[] array : coordinatesArray) {
            length += array.length;
            for (Point3D point : array) {
                if (point == null) {
                    length--;
                    continue;
                }
                x += point.getX();
                y += point.getY();
                z += point.getZ();
            }
        }
        System.err.println("x: " + x + "; length: " + length);
        Point3D center =  new Point3D(x / (double) length, y / (double) length, z / (double) length);
        System.err.println("(fx3d) Center of group: " + center);
        System.err.println("(fx3d) nucleotide atom count: " + length);
        return center;
    }

    public static void setRotationPivot(Point3D pivot, Rotate... rotates) {
        for (Rotate rotate : rotates) {
            rotate.setPivotX(pivot.getX());
            rotate.setPivotY(pivot.getY());
            rotate.setPivotZ(pivot.getZ());
        }
    }

    public static Group coatGroup3D(Group group, Color color, double size) {
        Group coatedGroup = new Group();
        System.err.println("coatGroup3D");
        coatGroup3dRecursion(coatedGroup, group, color, size);
        return coatedGroup;
    }

    public static Group coatGroup3dRecursion(Group coatedGroup, Group group, Color color, double size) {
        System.err.println("coatGroup3dRecursion. #Children: " + group.getChildren().size());
        for (Node node : group.getChildren()) {
            if (node instanceof Group)
                coatGroup3dRecursion(coatedGroup, ((Group) node), color, size);
            if (node instanceof Shape3D) {
                System.err.println("is Shape");
                coatedGroup.getChildren().add(coatShape3D((Shape3D) node, color, size));
            }
        }
        return coatedGroup;
    }

    public static Shape3D coatShape3D(Shape3D shape, Color color, double size) {
        if (shape instanceof Box) {
            Box coat = new Box(
                    ((Box) shape).getWidth() * size,
                    ((Box) shape).getHeight() * size,
                    ((Box) shape).getDepth() * size);
            coat.setMaterial(new PhongMaterial(color));
            copyTransforms(shape, coat);
            return coat;
        }
        if (shape instanceof Cylinder) {
            Cylinder coat = new Cylinder(
                    ((Cylinder) shape).getRadius() * size,
                    ((Cylinder) shape).getHeight());
            copyTransforms(shape, coat);
            coat.setMaterial(new PhongMaterial(color));

            System.err.println("return Cylinder");
            return coat;
        }
        if (shape instanceof MeshView) {
            MeshView coat = new MeshView(
                    ((MeshView) shape).getMesh());
            copyTransforms(shape, coat);
            coat.setMaterial(new PhongMaterial(color));
            System.err.println("return MeshView");
            return coat;
        }
        if (shape instanceof Sphere) {
            Sphere coat = new Sphere(
                    ((Sphere) shape).getRadius() * size);
            copyTransforms(shape, coat);
            coat.setMaterial(new PhongMaterial(color));
            System.err.println("return Sphere");
            return coat;
        }
        System.err.print("is no box no cylinder no meshview no sphere");
        return null;
    }

    private static void copyTransforms(Shape3D from, Shape3D to) {
        to.setTranslateX(from.getTranslateX());
        to.setTranslateY(from.getTranslateY());
        to.setTranslateZ(from.getTranslateZ());
        to.getTransforms().addAll(from.getTransforms());
    }
}
