package simple3dviewer;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.awt.*;

/**
 * Created by Joachim on 03/12/2015.
 */
public class View {
    private Scene scene;
    private final PerspectiveCamera camera =  new PerspectiveCamera(true);

    private final StackPane shackPlane = new StackPane();
    private Pane topPane = new Pane();
    private SubScene subScene;

    private final Group object = new Group();

    private final Box box1 = new Box(100,150,100);
    private final Box box2 = new Box(100,150,100);
    private final Cylinder cylinder = new Cylinder(40,300);

    private Rectangle bbb1, bbb2, bbc;

    private final Button button1 = new Button("Click me (or not, I don't care)");
    private final Button button2 = new Button("feeling dizzy, do not rotate");

    private Rotate rotationX = new Rotate(0, new Point3D(0,1,0));
    private Rotate rotationY = new Rotate(0, new Point3D(1,0,0));
    private Rotate rotationZ = new Rotate(0, new Point3D(0,0,1));
    private Scale scale = new Scale(1,1,1);

    private Point3D center;

    public View() {
        initCamera();
        initObject();
        initButtons();
        scene = new Scene(shackPlane, 800, 500, true);
        subScene = new SubScene(object, 800,500, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        shackPlane.getChildren().addAll(subScene, topPane);
        scene.setFill(Color.DARKGRAY);
        setDrag();
        updateScreenCenter();
        //updateRotation(500);

    }

    public void updateBoundingBoxes() {
        bbb1 = getBoundingBox2D(box1);
        bbb2 = getBoundingBox2D(box2);
        bbc = getBoundingBox2D(cylinder);
        topPane.getChildren().clear();
        topPane.getChildren().addAll(bbb1,bbb2,bbc,button1);
    }


    public void show(Stage primaryStage) {
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple3DViewer");
        primaryStage.show();


        updateBoundingBoxes();
    }

    public void initButtons() {
        topPane.getChildren().add(button1);
        object.getChildren().add(button2);
        button2.setTranslateX(40);
    }

    public void initCamera() {
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-500);
//        camera.getTransforms().addAll(rotationX,rotationY,rotationZ, scale);
        camera.getTransforms().add(scale);
    }

    public void initObject() {
        PhongMaterial material = new PhongMaterial(Color.ANTIQUEWHITE);
        PhongMaterial cylMaterial = new PhongMaterial(Color.CRIMSON);
        PhongMaterial material2 =  new PhongMaterial(Color.BEIGE);

        box1.drawModeProperty().set(DrawMode.FILL);
        box1.setMaterial(material);
        box1.setTranslateY(-150);
        Tooltip.install(box1, new Tooltip("Box1"));

        box2.drawModeProperty().set(DrawMode.FILL);
        box2.setMaterial(material2);
        box2.setTranslateY(150);
        Tooltip.install(box2, new Tooltip("Box2"));

        cylinder.drawModeProperty().set(DrawMode.FILL);
        cylinder.setMaterial(cylMaterial);
        Tooltip.install(cylinder, new Tooltip("Cylinder"));
        object.getChildren().addAll(box1, box2, cylinder);

        object.getTransforms().addAll(rotationX,rotationY);
    }

    public double getSphereZ(double deltaX, double deltaY) {
        double result = Math.sqrt(Math.pow(1000,2)-Math.pow(deltaX,2)-Math.pow(deltaY,2))/1000;
        if (result < 0.1 || Double.isNaN(result))
            return 0.1;
        return result;
    }

    public void updateRotation(double z) {
        rotationX.setPivotZ(-1*z);
        rotationY.setPivotZ(-1*z);
        rotationZ.setPivotZ(-1*z);
    }

    public void updateScreenCenter() {
        this.center =  new Point3D(this.scene.getWidth()/2, this.scene.getHeight()/2,0.0);
    }

    public static javafx.scene.shape.Rectangle getBoundingBox2D(Node shape) {
        final javafx.stage.Window window = shape.getScene().getWindow();
        final Bounds bounds = shape.getBoundsInLocal();
        final Bounds screenBounds = shape.localToScreen(bounds);
        javafx.scene.shape.Rectangle rectangle = new javafx.scene.shape.Rectangle (
                (int) Math.round(screenBounds.getMinX() - window.getX()),
                (int) Math.round(screenBounds.getMinY() - window.getY())-20,
                (int) Math.round(screenBounds.getWidth()),
                (int) Math.round(screenBounds.getHeight()));
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.LIGHTBLUE);
        rectangle.setMouseTransparent(true);
        return rectangle;
    }


    public void setDrag() {
        class Delta{double x,y,z;}
        final Delta dragDelta = new Delta();
        final Delta angleDelta = new Delta();
        final Delta scaleDelta = new Delta();
        final Delta mouseMoveDelta = new Delta();
        scene.setOnMousePressed((me) -> {
            dragDelta.x = me.getSceneX();
            dragDelta.y = me.getSceneY();
            angleDelta.x = rotationX.getAngle();
            angleDelta.y = rotationY.getAngle();
//            angleDelta.z = rotationZ.getAngle();
        });
        scene.setOnMouseDragged((me) -> {
            double xCoordinate = me.getSceneX() - dragDelta.x;
            double yCoordinate = me.getSceneY() - dragDelta.y;
//            double zCoordinate = Math.sqrt(Math.pow(xCoordinate,2)+Math.pow(yCoordinate,2));
            double z = getSphereZ(me.getSceneX()-center.getX(), me.getSceneY()-center.getY());
            z = Math.pow(z,2);
            rotationX.setAngle(angleDelta.x + xCoordinate * z / 2);
            rotationY.setAngle(angleDelta.y - yCoordinate * z / 2);
//            rotationZ.setAngle(angleDelta.z + zCoordinate * z / 2);
            updateBoundingBoxes();
        });

        //Scale Magic
        scene.setOnKeyPressed((me) -> {
            scaleDelta.y = scale.getY();
            scaleDelta.x = camera.getTranslateZ();
            mouseMoveDelta.y = MouseInfo.getPointerInfo().getLocation().getY();
        });
        scene.setOnMouseMoved((me) -> {
            if (me.isShiftDown()) {
                double scaleValue = (MouseInfo.getPointerInfo().getLocation().getY() - mouseMoveDelta.y);
                camera.setTranslateZ(scaleDelta.x + scaleValue*2);
                //updateRotation(scaleDelta.x+scaleValue*2);
                updateBoundingBoxes();
            }
        });
    }
}
