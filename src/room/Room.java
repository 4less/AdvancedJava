package room;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.awt.*;


/**
 * Created by Joachim on 11/12/2015.
 */
public class Room extends SubScene {
    private final Rotate rotateX = new Rotate(0, new Point3D(0,1,0));
    private final Rotate rotateY = new Rotate(0, new Point3D(1,0,0));
    private final Rotate rotateZ = new Rotate(0, new Point3D(0,0,1));

    private Point3D center;

    private final Group object = new Group();

    public Room(Group object, double width, double height) {
        super(object, width, height, true, SceneAntialiasing.BALANCED);
        this.setRoot(this.object);
        this.setFill(Color.BLACK);
        initCamera();
        updateSceneCenter();
        this.object.getChildren().add(object);
        centerObject();
    }

    private class CenterDev {
        double x,y,z;
        int count;

        public CenterDev() {
            x = 0;
            y = 0;
            z = 0;
            count = 0;
        }

        public void print() {
            System.out.println(x + " : " + y + " : " + z);
        }

        public void computeDev() {
            x = -1 * x/count;
            y = -1 * y/count;
            z = -1 * z/count;
        }
    }

    public void centerObject() {
        CenterDev center = new CenterDev();
        centerRecursion(object, center);

        center.print();
        center.computeDev();
        center.print();
//        object.setTranslateX(center.x);
//        object.setTranslateY(center.y);
//        object.setTranslateZ(center.z);

//        centerRecursion(object, center.x, center.y, center.z);
    }

    public void centerRecursion(Group node, CenterDev center) {
        if (!node.getChildren().isEmpty()) {
            if (!(node.getChildren().get(0) instanceof Group)) {
                for (Node element : node.getChildren()) {
                    center.x += element.getTranslateX();
                    center.y += element.getTranslateY();
                    center.z += element.getTranslateZ();
                    center.count++;
                }
            } else {
                for (Object group : node.getChildren())
                    centerRecursion((Group) group, center);
            }
        }
    }

    public void centerRecursion(Group node, Double x, Double y, Double z) {
        if (!node.getChildren().isEmpty()) {
            if (!(node.getChildren().get(0) instanceof Group)) {
                for (Node element : node.getChildren()) {
                    element.setTranslateX(x);
                    element.setTranslateY(y);
                    element.setTranslateZ(z);
                }
            } else {
                for (Object group : node.getChildren())
                    centerRecursion((Group) group, x, y, z);
            }
        }
    }

    public void alignToParent(Pane pane) {
        this.widthProperty().bind(pane.widthProperty());
        this.heightProperty().bind(pane.heightProperty());
    }

    private static double getSphereZ(double deltaX, double deltaY) {
        double result = Math.sqrt(Math.pow(1000, 2) - Math.pow(deltaX, 2) - Math.pow(deltaY, 2)) / 1000;
        if (result < 0.1 || Double.isNaN(result))
            return 0.1;
        return result;
    }

    public void updateSceneCenter() {
        this.center = new Point3D(this.getWidth()/2, this.getHeight()/2,0.0);
    }

    public void rotateObject(Scene scene) {
        this.getRoot().getTransforms().addAll(rotateX,rotateY);
        final Delta dragDelta = new Delta();
        final Delta angleDelta = new Delta();
        scene.setOnMousePressed((me) -> {
            dragDelta.x = me.getSceneX();
            dragDelta.y = me.getSceneY();
            angleDelta.x = rotateX.getAngle();
            angleDelta.y = rotateY.getAngle();
        });
        scene.setOnMouseDragged((me) -> {
            double xCoordinate = me.getSceneX() - dragDelta.x;
            double yCoordinate = me.getSceneY() - dragDelta.y;
            double z = getSphereZ(me.getSceneX() - center.getX(), me.getSceneY() - center.getY());
            z = Math.pow(z, 2);
            rotateX.setAngle(angleDelta.x + xCoordinate * z / 2);
            rotateY.setAngle(angleDelta.y - yCoordinate * z / 2);
        });

    }

    public void rotateCamera() {

    }

    public void scaleCamera(Scene scene) {
        final Delta mouseMoveDelta = new Delta();
        final Delta scaleDelta = new Delta();
        scene.setOnKeyPressed((me) -> {
            scaleDelta.x = this.getCamera().getTranslateZ();
            mouseMoveDelta.y = MouseInfo.getPointerInfo().getLocation().getY();
        });
        scene.setOnMouseMoved((me) -> {
            if (me.isShiftDown()) {
                double scaleValue = (MouseInfo.getPointerInfo().getLocation().getY() - mouseMoveDelta.y);
                this.getCamera().setTranslateZ(scaleDelta.x + scaleValue * 2);
            }
        });
    }

    private void initCamera() {
        this.setCamera(new PerspectiveCamera(true));
        this.getCamera().setNearClip(0.1);
        this.getCamera().setFarClip(10000.0);
        this.getCamera().setTranslateZ(-500);
    }

    class Delta {
        double x, y, z;
    }
}
