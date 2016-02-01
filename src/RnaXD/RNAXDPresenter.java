package rnaxd;

import clusteringviewer.StageManager;
import dnamanipulator.view.TypeAlert;
import io.NucleotideType;
import io.PDBParser;
import io.Sequence;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import molecules.*;
import moleview.Molecule3D;
import moleview.NucleotideMeshs;
import rna1d.BaseBox;
import rna1d.RnaSequenceView;
import rna2d.Nussinov;
import rna2d.RnaStructureGraph;
import rna2d.SpringEmbedder;
import room.Room;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jogi on 20.01.2016.
 */
public class RNAXDPresenter implements Initializable {
    private static final String title = "RNAXD RNA Structure Viewer for 1D 2D and 3D";
    private RNAXDModel model;
    private Scene scene;
    private FileChooser fileChooser = new FileChooser();
    private NucleotideSelectionModel<Nucleotide> selectionModel;


    // MainMenus
    @FXML private MenuItem openItem;
    @FXML private MenuItem closeItem;
    @FXML private CheckMenuItem augcItem;
    @FXML private CheckMenuItem pyrpurItem;
    @FXML private CheckMenuItem meshItem;
    @FXML private CheckMenuItem stickItem;
    @FXML private CheckMenuItem ballItem;
    @FXML private CheckMenuItem reloadItem;

    // MainAreas
    @FXML private Pane pane2D;
    @FXML private Pane pane3D;
    //@FXML private GridPane gridPane;
    private Room secondaryRoom;
    private Room tertiaryRoom;
    @FXML private HBox primaryBox;
    @FXML private TextArea errorArea;


    // Buttons on the right side;
    @FXML private ToggleButton colorPyrpur;
    @FXML private ToggleButton colorAugc;
    @FXML private ToggleButton structureMesh;
    @FXML private ToggleButton structureStick;
    @FXML private ToggleButton structureBall;
    @FXML private ToggleButton playRotate;
    @FXML private Button rotateLeft;
    @FXML private Button rotateRight;
    @FXML private Button zoomIn;
    @FXML private Button zoomOut;
    @FXML private Button centerObject;

    private RNASequence rnaSequence3D;

    public void show(Parent root, Stage primaryStage) throws IOException {
        primaryStage.getIcons().add(new Image("rnaxd/images/rxd_logo.png"));
        primaryStage.setTitle(title);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        setOpenFileEvent(primaryStage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        model = new RNAXDModel();

        disableButtonBinding();
        bindOptions();
        model.setFileLoaded(false);
        reloadItem.setSelected(false);
        initFileChooser();
        setFileOpenedBinding();
    }

    // Set Open File Event
    public void setOpenFileEvent(Stage primaryStage) {

        openItem.setOnAction(value -> {
            File selectedFile = fileChooser.showOpenDialog(
                    primaryStage);
            model.setFileLoaded(false);
            if (selectedFile != null) {
                if (selectedFile.exists() && !selectedFile.isDirectory()) {
                    try {
                        this.model.setPdbfile(PDBParser.parse(selectedFile));
                        this.model.setFileLoaded(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void initFileChooser() {
        FileChooser.ExtensionFilter pdbFilter = new FileChooser
                .ExtensionFilter("PDB Files", "*.pdb");
        this.fileChooser.setTitle("Open Fasta File");
        this.fileChooser.getExtensionFilters().add(pdbFilter);
    }

    public void setFileOpenedBinding() {
        model.fileLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                initTertiaryRoom();
                initPrimaryView();
            }
        });
    }

    private void initSelectionModel() {
        this.selectionModel = new NucleotideSelectionModel(rnaSequence3D.getNucleotides().toArray(new Nucleotide[rnaSequence3D.getNucleotides().size()]));
    }

    private void bindSelection() {
        rnaSequence3D.getSelectionModel().getSelectedIndices();
    }

    private void bindOptions() {
        pyrpurItem.selectedProperty().bindBidirectional(colorPyrpur.selectedProperty());
        augcItem.selectedProperty().bindBidirectional(colorAugc.selectedProperty());
        meshItem.selectedProperty().bindBidirectional(structureMesh.selectedProperty());
        stickItem.selectedProperty().bindBidirectional(structureStick.selectedProperty());
    }

    private void activateSelectionFocus() {

        selectionModel.getSelectedItems().addListener((ListChangeListener<Nucleotide>) c -> {
            System.err.println("Selection performed");
            //System.err.println("select(" + selectionModel.getSelectedIndex() + ")");
            if (selectionModel.getSelectedItems().isEmpty()) {
                System.err.println("Empty List of Indices");
                tertiaryRoom.setCameraCenter(rnaSequence3D.computeCenter());
            }
            if (selectionModel.getSelectedIndices().size() > 0) {
                System.err.println(selectionModel.getSelectedIndices());
                Point3D p = new Point3D(0, 0, 0);

                Nucleotide n = selectionModel.getItems()[selectionModel.getSelectedIndices().get(selectionModel.getSelectedIndices().size()-1)];
                p = p.add(utils.fx3d.computeCenter(n.getNucleotideCoordinates()));
                p = p.add(utils.fx3d.computeCenter(n.getBaseCoordinates()));
                p = p.multiply(1 / ((double) 2));
                tertiaryRoom.setCameraCenter(p);
            }


        });
    }

    private void initPrimaryView() {
        RnaSequenceView primarySequence = new RnaSequenceView();
        try {
            primarySequence.processSequence(rnaSequence3D.extractString());
        } catch (IOException e) {
            System.err.println("Error loading rna primary sequence box from string");
            e.printStackTrace();
        }
        primarySequence.setSelectionModel(selectionModel);
        primaryBox.getChildren().addAll(primarySequence.getRnaSequence());
    }

    private void activateRightSideButtons() {
        colorPyrpur.setOnAction((me) -> {
            ((MeshModel) rnaSequence3D.getModels().get(1)).colorPyrPur();
        });
        colorAugc.setOnAction((me) -> {
            ((MeshModel) rnaSequence3D.getModels().get(1)).colorBases();
        });
        centerObject.setOnAction((me) -> {
            tertiaryRoom.setCameraCenter(rnaSequence3D.computeCenter());
        });
    }

    private void loadAllmodels() {
        rnaSequence3D.addModel(new StickModel());
        rnaSequence3D.addModel(new MeshModel());
        rnaSequence3D.addModel(new BallModel());

        structureStick.selectedProperty().unbind();
        structureMesh.selectedProperty().unbind();
        structureBall.selectedProperty().unbind();

        rnaSequence3D.getModels().get(0).visibleProperty().bind(structureStick.selectedProperty());
        rnaSequence3D.getModels().get(1).visibleProperty().bind(structureMesh.selectedProperty());
        rnaSequence3D.getModels().get(2).visibleProperty().bind(structureBall.selectedProperty());

        tertiaryRoom.getObject().getChildren().addAll(rnaSequence3D.getModels().get(0));
        tertiaryRoom.getObject().getChildren().addAll(rnaSequence3D.getModels().get(1));
        tertiaryRoom.getObject().getChildren().addAll(rnaSequence3D.getModels().get(2));
    }

    private void clearModels() {
        structureStick.selectedProperty().unbind();
        structureBall.selectedProperty().unbind();
        structureMesh.selectedProperty().unbind();

        structureBall.setOnAction(event -> {

        });

        rnaSequence3D.getModels().clear();
    }

    private void loadOneModel(int model) {
        switch(model) {
            case 0:
                rnaSequence3D.addModel(new StickModel());
                break;
            case 1:
                rnaSequence3D.addModel(new MeshModel());
                break;
            case 2:
                rnaSequence3D.addModel(new BallModel());
                break;
        }
        tertiaryRoom.getObject().getChildren().addAll(rnaSequence3D.getModels().get(0));
    }

    private void initTertiaryRoom() {
        Group models3D = new Group();
        rnaSequence3D = new RNASequence(model.getPdbfile());

        tertiaryRoom = new Room(models3D, 50, 50);
        tertiaryRoom.rotateCamera(tertiaryRoom);
        tertiaryRoom.scaleCamera(scene);
        pane3D.getChildren().add(tertiaryRoom);
        tertiaryRoom.alignToParent(pane3D);
        tertiaryRoom.setPickOnBounds(false);

        if (reloadItem.isSelected()) loadOneModel(0);
        if (!reloadItem.isSelected()) loadAllmodels();
        ((MeshModel) rnaSequence3D.getModels().get(1)).colorPyrPur();

        initSelectionModel();
        rnaSequence3D.setSelectionModel(selectionModel);

        activateSelectionFocus();
        activateRightSideButtons();
    }

    private void testRnaSecondaryView() throws IOException {

    }

    private void disableButtonBinding() {
        //MenuItems
        pyrpurItem.disableProperty().bind(model.fileLoadedProperty().not());
        augcItem.disableProperty().bind(model.fileLoadedProperty().not());
        meshItem.disableProperty().bind(model.fileLoadedProperty().not());
        stickItem.disableProperty().bind(model.fileLoadedProperty().not());
        ballItem.disableProperty().bind(model.fileLoadedProperty().not());

        //Buttons on the side
        colorPyrpur.disableProperty().bind(model.fileLoadedProperty().not());
        colorAugc.disableProperty().bind(model.fileLoadedProperty().not());
        structureMesh.disableProperty().bind(model.fileLoadedProperty().not());
        structureStick.disableProperty().bind(model.fileLoadedProperty().not());
        structureBall.disableProperty().bind(model.fileLoadedProperty().not());
        playRotate.disableProperty().bind(model.fileLoadedProperty().not());
        rotateLeft.disableProperty().bind(model.fileLoadedProperty().not());
        rotateRight.disableProperty().bind(model.fileLoadedProperty().not());
        zoomIn.disableProperty().bind(model.fileLoadedProperty().not());
        zoomOut.disableProperty().bind(model.fileLoadedProperty().not());
        centerObject.disableProperty().bind(model.fileLoadedProperty().not());
    }
}

