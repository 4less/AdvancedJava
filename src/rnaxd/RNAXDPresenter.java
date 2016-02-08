package rnaxd;

import clusteringviewer.StageManager;
import io.PDBParser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import molecules.*;
import rna1d.RnaSequenceView;
import room.Room;
import selection.NucleotideMouseSelection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jogi on 20.01.2016.
 */

public class RNAXDPresenter implements Initializable {
    private static final String title = "RNAXD RNA Structure Viewer for 1D 2D and 3D";
    private rnaxd.RNAXDModel model;
    private Scene scene;
    private FileChooser fileChooser = new FileChooser();
    private NucleotideSelectionModel<Nucleotide> selectionModel;

    private ListChangeListener<Nucleotide> selectionFocusListener;
    private EventHandler<KeyEvent> keyPressedEventHandler;

    private View2DController view2DController = new View2DController();
    private RNASequence rnaSequence3D;

    // MainMenus
    @FXML private MenuItem openItem;
    @FXML private MenuItem closeItem;
    @FXML private CheckMenuItem augcItem;
    @FXML private CheckMenuItem pyrpurItem;
    @FXML private CheckMenuItem meshItem;
    @FXML private CheckMenuItem stickItem;
    @FXML private CheckMenuItem ballItem;
    @FXML private CheckMenuItem reloadItem;
    @FXML private CheckMenuItem selectionFocusItem;

    // MainAreas
    @FXML private Pane pane2D;
    @FXML private Pane pane3D;
    //@FXML private GridPane gridPane;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new RNAXDModel();
        setFileDependingButtonBindings();
        bindMenuItemsToButtons();

        model.setFileLoaded(false);
        reloadItem.setSelected(false);
        selectionFocusItem.setSelected(false);

        initFileChooser();
        setFileOpenedBinding();
    }

    /**
     * set scene of primarystage and init events that need the primaryStage
     * @param root
     * @param primaryStage
     * @throws IOException
     */
    public void show(Parent root, Stage primaryStage) throws IOException {
        primaryStage.getIcons().add(new Image("rnaxd/images/rxd_logo.png"));
        primaryStage.setTitle(title);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        setOpenFileEvent(primaryStage);
        setOnCloseEvent(primaryStage);
    }


    /**
     * Inizializes the filechooser that loads the pdbfile
     * @param primaryStage
     */
    public void setOpenFileEvent(Stage primaryStage) {
        openItem.setOnAction(value -> {
            File selectedFile = fileChooser.showOpenDialog(
                    primaryStage);
            if (selectedFile.exists() && !selectedFile.isDirectory()) {
                try {
                    onPdbFileOpened(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Set action that happens on button "close" press
     * @param primaryStage
     */
    public void setOnCloseEvent(Stage primaryStage) {
        closeItem.setOnAction(value ->
                primaryStage.close());
    }

    /**
     * Call the method loadProgramDataFromPdb()
     * that loads the whole program functioning (views, keyhandlers, etc)
     */
    public void setFileOpenedBinding() {
        model.fileLoadedProperty().addListener(
                (observable, oldValue, newValue) -> {
            if (newValue == true)
                loadProgramDataFromPdb();
        });
    }
    
    /**
     * Sets the pdb file in the model and forces a fileloadedproperty fire
     * @param selectedFile
     * @throws IOException
     */
    private void onPdbFileOpened(File selectedFile) throws IOException {
        getModel().setPdbfile(PDBParser.parse(selectedFile));
        getModel().fileReloaded();
    }

    /**
     * Method that loads all necessary information and initiates
     * the views, the keyhandlers etc.
     */
    public void loadProgramDataFromPdb() {
        // Extract nucleotide information
        rnaSequence3D = new RNASequence(getModel().getPdbfile());

        //Extract base-sequence and compute watson-crick bonds
        getModel().setSequence(rnaSequence3D.extractString());
        getModel().setBonds(rnaSequence3D.computeWCBonds());

        //Init selection Model
        initSelectionModel();

        //Init the Views
        initPrimaryView();
        initSecondaryView();
        initTertiaryView();

        //init focus selection listener
        initSelectionListener();
        selectionFocusBinding();

        //init keyhandler
        if (keyPressedEventHandler != null)
            scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        initKeyEventHandler();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
    }

    /* #######################################################################
     *      Methods that initialize Objects and can just be performed when
     *      the pdb-file is loaded.
    ########################################################################*/
    
    public void initFileChooser() {
        FileChooser.ExtensionFilter pdbFilter = new FileChooser
                .ExtensionFilter("PDB Files", "*.pdb");
        this.fileChooser.setTitle("Open Fasta File");
        this.fileChooser.getExtensionFilters().add(pdbFilter);
    }
    
    private void initSelectionModel() {
        this.selectionModel = new NucleotideSelectionModel(
                rnaSequence3D.getNucleotides().toArray(
                        new Nucleotide[rnaSequence3D.getNucleotides().size()]));
    }

    private void initSelectionListener() {
        selectionFocusListener = c -> {
            if (selectionModel.getSelectedItems().isEmpty())
                tertiaryRoom.setCameraCenter(rnaSequence3D.computeCenter());
            if (selectionModel.getSelectedIndices().size() > 0)
                selectFocus();
        };
    }

    private void initKeyEventHandler() {
        keyPressedEventHandler = event -> {
            if (event.getCode() == KeyCode.LEFT) {
                System.out.println("LEFT");
                int focus = selectionModel.getFocusIndex();
                selectionModel.deselect(focus);
                if (focus-1 >= 0) {
                    selectionModel.select(focus-1);
                }
                event.consume();
            }
            if (event.getCode() == KeyCode.RIGHT) {
                System.out.println("RIGHT");
                int focus = selectionModel.getFocusIndex();
                selectionModel.deselect(focus);
                if (focus+1 < selectionModel.getItems().length) {
                    selectionModel.select(focus+1);
                }
                event.consume();
            }
        };
    }

    //######################### INITIALIZE VIEWS #############################

    /**
     * initialize primary view items
     */
    private void initPrimaryView() {
        // clear primary view items on initializion
        primaryBox.getChildren().clear();

        // initialize primarySequence element to create primary view items
        RnaSequenceView primarySequence = new RnaSequenceView();
            try {
                primarySequence.processSequence(getModel().getSequence());
            } catch (IOException e) {
                System.err.println("Error loading rna primary sequence box from string");
                e.printStackTrace();
            }

        // Set the selection model
        primarySequence.setSelectionModel(selectionModel);

        // Add all nucleotide items to the primaryBox
        primaryBox.getChildren().addAll(primarySequence.getRnaSequence());

        NucleotideMouseSelection.installOnNucleotides(selectionModel,primarySequence.getRnaSequence(), getModel().getBonds());
    }

    /**
     * initialize secondary structure view items
     */
    private void initSecondaryView() {
        // clear the secondary structure pane on initialization
        pane2D.getChildren().clear();

        if (view2DController.getGraph2d() != null) {
            pane2D.widthProperty().removeListener(view2DController.getWidthChangeListener());
            pane2D.heightProperty().removeListener(view2DController.getHeightChangeListener());
        }

        view2DController.initGraph2d(
                rnaSequence3D.extractString(),
                rnaSequence3D.getNucleotides().size(),
                rnaSequence3D.computeWCBonds(),
                (int) pane2D.getWidth(),
                (int) pane2D.getHeight());


        listenOnPane2dResize();
        view2DController.getGraph2d().setSelectionModel(selectionModel);
        pane2D.getChildren().add(view2DController.getGraph2d());
    }

    /**
     * initialize tertiary structure view
     */
    private void initTertiaryView() {
        Group models3D = new Group();
        rnaSequence3D = new RNASequence(model.getPdbfile());


        if (tertiaryRoom == null) {
            tertiaryRoom = new Room(models3D, 50, 50);
            tertiaryRoom.rotateCamera(tertiaryRoom);
            tertiaryRoom.scaleCamera(scene);
            tertiaryRoom.alignToParent(pane3D);
            tertiaryRoom.setPickOnBounds(false);
            pane3D.getChildren().add(tertiaryRoom);
        } else {
            tertiaryRoom.setObject(rnaSequence3D);
        }

        if (reloadItem.isSelected()) loadOneModel(0);
        if (!reloadItem.isSelected()) loadAllmodels();
        ((MeshModel) rnaSequence3D.getModels().get(1)).colorPyrPur();

        rnaSequence3D.setSelectionModel(selectionModel);
        tertiaryRoom.setCameraCenter(rnaSequence3D.computeCenter());

        activateRightSideButtons();
    }

    private void listenOnPane2dResize() {
        view2DController.initProperties();

        pane2D.widthProperty().addListener(view2DController.getWidthChangeListener());
        pane2D.heightProperty().addListener(view2DController.getHeightChangeListener());
    }


    private void bindMenuItemsToButtons() {
        pyrpurItem.selectedProperty().bindBidirectional(colorPyrpur.selectedProperty());
        augcItem.selectedProperty().bindBidirectional(colorAugc.selectedProperty());
        meshItem.selectedProperty().bindBidirectional(structureMesh.selectedProperty());
        stickItem.selectedProperty().bindBidirectional(structureStick.selectedProperty());
    }


    private void selectionFocusBinding() {
        if (selectionFocusItem.isSelected()) {
            selectionModel.getSelectedItems().addListener(selectionFocusListener);
        }

        selectionFocusItem.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
            if (newValue == oldValue) return;
            if (newValue && !oldValue) {
                selectionModel.getSelectedItems().addListener(selectionFocusListener);
            } else {
                selectionModel.getSelectedItems().removeListener(selectionFocusListener);
            }
        });
    }

    private void selectFocus() {
        Point3D p = new Point3D(0,0,0);
        Nucleotide n = selectionModel.getItems()[selectionModel.getSelectedIndices().get(selectionModel.getSelectedIndices().size()-1)];
        p = p.add(utils.fx3d.computeCenter(n.getNucleotideCoordinates()));
        p = p.add(utils.fx3d.computeCenter(n.getBaseCoordinates()));
        p = p.multiply(1 / ((double) 2));
        //fx3d.transiteFromTOo(tertiaryRoom.getCamera(), p);
        tertiaryRoom.setCameraCenter(p);
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
    

    private void setFileDependingButtonBindings() {
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

    public RNAXDModel getModel() {
        return model;
    }
}

