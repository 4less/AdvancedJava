package moleview;

import io.PDBParser;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import room.Room;

import javax.swing.border.Border;
import java.io.File;
import java.io.IOException;

/**
 * Created by Joachim on 10/12/2015.
 */
public class MoleViewView {
    private final String title = "MoleView: Dig into the structure!";

    private Scene scene;

    private final MenuBar menuBar = new MenuBar();
    private final Menu file = new Menu("file");
    private final MenuItem open = new MenuItem("Open");
    private final MenuItem exit = new MenuItem("Exit");

    private final Menu display = new Menu("display");
    private final MenuItem stickmodel = new MenuItem("StickModel");
    private final MenuItem stickspheremodel = new MenuItem("Sphere and Stick Model");
    private final MenuItem rnaMeshModel = new MenuItem("rnaMeshModel");

    private final BorderPane root = new BorderPane();

    private final StackPane shackPlane = new StackPane();
    private Pane topPane = new Pane();
    private Room room;


    private Group object;
    private Molecule3D stickModelObject;

    private Point3D center;

    public MoleViewView() throws IOException {
        object = new NucleotideMeshs(PDBParser.parse(new File("AUGC.pdb")));
        stickModelObject = new Molecule3D(PDBParser.parse(new File("AUGC.pdb")));
        stickModelObject.build3DModel(1.7);

        //buildTestMesh();

        Group objects = new Group();

        objects.getChildren().addAll(object, stickModelObject);

        scene = new Scene(root, 800, 500, true);
        root.setTop(menuBar);
        root.setCenter(shackPlane);
        room = new Room(objects, 800, 500);
        room.rotateObject(scene);
        room.scaleCamera(scene);


//        menuBar.getMenus().addAll(file,display);
//        file.getItems().addAll(open,exit);
//        display.getItems().addAll(stickmodel, stickspheremodel);

        shackPlane.getChildren().addAll(room, topPane);
        scene.setFill(Color.DARKGRAY);



    }

    public void show(Stage primaryStage) {
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
    }
}