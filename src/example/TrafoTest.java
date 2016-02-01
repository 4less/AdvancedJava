package example;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class TrafoTest extends Application {

    final Group root = new Group();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final XformCamera cameraXform = new XformCamera();
    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;

    @Override
    public void start(Stage primaryStage) {
        //root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildBodySystem();
        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.GREY);
        handleMouse(scene);
        primaryStage.setTitle("Transformationen");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);

        cameraXform.t.setX(200);
        cameraXform.t.setY(100);
        cameraXform.t.setZ(50);
        Sphere center = new Sphere(20);
        center.setMaterial(new PhongMaterial(Color.BLACK));
        center.setTranslateX(200);
        center.setTranslateY(100);
        center.setTranslateZ(50);
        root.getChildren().add(center);
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    private void buildBodySystem() {
        PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
        Box box = new Box(400, 200, 100);
        box.setMaterial(whiteMaterial);
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        Sphere sphere = new Sphere(5);
        sphere.setMaterial(redMaterial);
        sphere.setTranslateZ(-50.0);
        root.getChildren().addAll(box);
        root.getChildren().addAll(sphere);
    }

    private void handleMouse(Scene scene) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
            // this is done after clicking and the rotations are apearently
            // performed in coordinates that are NOT rotated with the camera.
            // (pls activate the two lines below for clicking)
            //cameraXform.rx.setAngle(-90.0);
            //cameraXform.ry.setAngle(180.0);
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown()) {
                // this is done when the mouse is dragged and each rotation is
                // performed in coordinates, that are rotated with the camera.
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() + mouseDeltaX * 0.2);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() - mouseDeltaY * 0.2);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}


class XformCamera extends Group {

    final Translate t = new Translate(0.0, 0.0, 0.0);
    final Rotate rx = new Rotate(0,0, 0, 0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    public XformCamera() {
        super();
        this.getTransforms().addAll(t, rx, ry, rz);
    }

}