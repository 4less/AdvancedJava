package moleview;

import io.PDBParser;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import room.Room;
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

    private HBox statusBar = new HBox();

    private Group object;
    private Molecule3D stickModelObject;

    private Point3D center;

    private DoubleBinding widthProperty;
    private DoubleBinding heightProperty;

    public MoleViewView() throws IOException {
        String filename = "2fdt.pdb";
        object = new NucleotideMeshs(PDBParser.parse(new File(filename)));
//        object = new NucleotideMeshs(PDBParser.parse(new File("AUGC.pdb")));
        //stickModelObject = new Molecule3D(PDBParser.parse(new File("AUGC.pdb")));
        //stickModelObject.build3DModel(1.7);

        //buildTestMesh();

        Group objects = new Group();

        objects.getChildren().addAll(object);

        scene = new Scene(shackPlane, 800, 500, true);
        root.setTop(menuBar);
        //root.setCenter(shackPlane);
        root.setBottom(statusBar);
        statusBar.getChildren().add(new Label(filename));
        //statusBar.setStyle("-fx-background-color: #FFFFFF;");
        room = new Room(objects, 800, 500);
        room.rotateObject(scene);
        room.scaleCamera(scene);
        room.setFill(Color.WHITE);


//        menuBar.getMenus().addAll(file,display);
//        file.getItems().addAll(open,exit);
//        display.getItems().addAll(stickmodel, stickspheremodel);

        shackPlane.getChildren().addAll(room, root);
        scene.setFill(Color.DARKGRAY);
    }

    public void show(Stage primaryStage) {
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
    }
}