package room;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.awt.*;


/**
 * Created by Joachim on 11/12/2015.
 */
public class Room extends SubScene {
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    
    private static final double CAMERA_INITIAL_DISTANCE = -150;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    
    private final Rotate rotateX = new Rotate(0, new Point3D(0,1,0));
    private final Rotate rotateY = new Rotate(0, new Point3D(1,0,0));
    private final Rotate rotateZ = new Rotate(0, new Point3D(0,0,1));

    final XformCamera cameraXform = new XformCamera();

    private Point3D center;

    private final Group object = new Group();

    public Room(Group object, double width, double height) {
        super(object, width, height, true, SceneAntialiasing.BALANCED);
        this.setRoot(this.object);
        this.setFill(Color.WHITE);
        buildCamera();
        this.setCamera(camera);

        this.object.getChildren().add(object);
    }


    public Group getObject() {
        return object;
    }

    public void centerObject(Point3D translate) {
        System.err.println("Translate: " + translate);
        object.setTranslateX(translate.getX());
        object.setTranslateY(translate.getY());
        object.setTranslateZ(translate.getZ());
    }


    public void centerObject() {
        Double x = 0.0, y = 0.0, z = 0.0;
        Integer count = 0;
        centerRecursion(object, x, y, z, count);
        x = -1 * x/count;
        y = -1 * y/count;
        z = -1 * z/count;
        centerRecursion(object, x, y, z);
    }

    public void centerRecursion(Group node, Double x, Double y, Double z, Integer count) {
        if (!node.getChildren().isEmpty()) {
            if (!(node.getChildren().get(0) instanceof Group)) {
                for (Node element : node.getChildren()) {
                    x += element.getTranslateX();
                    y += element.getTranslateY();
                    z += element.getTranslateZ();
                    count++;
                }
            } else {
                for (Object group : node.getChildren())
                    centerRecursion((Group) group, x, y, z, count);
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

    public void rotateObject(SubScene scene) {
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
            double z = 1;
            //double z = getSphereZ(me.getSceneX() - center.getX(), me.getSceneY() - center.getY());
            //z = Math.pow(z, 2);
            rotateX.setAngle(angleDelta.x + xCoordinate * z / 2);
            rotateY.setAngle(angleDelta.y - yCoordinate * z / 2);
        });

    }

    public void rotateCamera(SubScene scene) {
        final Delta dragDelta = new Delta();
        final Delta dragDeltaOld = new Delta();
        final Delta mouseDelta = new Delta();
        scene.setOnMousePressed((MouseEvent me) -> {
            dragDelta.x = me.getSceneX();
            dragDelta.y = me.getSceneY();
            dragDeltaOld.x = me.getSceneX();
            dragDeltaOld.y = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            dragDeltaOld.x = dragDelta.x;
            dragDeltaOld.y = dragDelta.y;
            dragDelta.x = me.getSceneX();
            dragDelta.y = me.getSceneY();
            mouseDelta.x = (dragDelta.x - dragDeltaOld.x);
            mouseDelta.y = (dragDelta.y - dragDeltaOld.y);
            cameraXform.ry.setAngle(cameraXform.ry.getAngle() + mouseDelta.x * 0.2);
            cameraXform.rx.setAngle(cameraXform.rx.getAngle() - mouseDelta.y * 0.2);

        });
    }

    public void scaleCamera(Scene scene) {
        final Delta mouseMoveDelta = new Delta();
        final Delta scaleDelta = new Delta();
        scene.setOnKeyPressed((me) -> {
            scaleDelta.x = camera.getTranslateZ();
            mouseMoveDelta.y = MouseInfo.getPointerInfo().getLocation().getY();
        });
        this.setOnMouseMoved((me) -> {
            if (me.isShiftDown()) {
                double scaleValue = (MouseInfo.getPointerInfo().getLocation().getY() - mouseMoveDelta.y);
                System.out.println(scaleDelta.x + " + (" + MouseInfo.getPointerInfo().getLocation().getY() + " - " + mouseMoveDelta.y + ")");
                camera.setTranslateZ(scaleDelta.x + scaleValue * 2);
            }
        });
    }

    private void buildCamera() {
        object.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    public void setCameraCenter(Point3D center) {
        cameraXform.t.setX(center.getX());
        cameraXform.t.setY(center.getY());
        cameraXform.t.setZ(center.getZ());

        //cameraXform.t.transform(center.getX(), center.getY(), center.getZ());
    }

    class Delta {
        double x, y, z;
    }
}


class XformCamera extends Group {

    final Translate t = new Translate(0.0, 0.0, 0.0);
    final Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    public XformCamera() {
        super();
        this.getTransforms().addAll(t, rx, ry, rz);
    }

}
