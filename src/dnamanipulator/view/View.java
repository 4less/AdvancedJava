package dnamanipulator.view;

import io.Sequence;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by Joachim on 11/11/2015.
 */
public class View {
    private Scene scene;
    final private String title = "DR. NA Manipulator - Return of the Clones";

    private BorderPane root = new BorderPane();

    private ProgramMenuBar programMenuBar = new ProgramMenuBar();
    private FileChooser openFileChooser = new FileChooser();
    private FileChooser saveFileChooser = new FileChooser();

    private SplitPane mainPane = new SplitPane();
    private EditBox editBox = new EditBox();
    private NavigationBox navigation = new NavigationBox();

    private StatusBar statusBar = new StatusBar();

    private final ObjectProperty<Sequence> sequence = new SimpleObjectProperty<>(this, "sequence", null);

    public View() {
        // Initialize UI-Items
        initRootPane();
        initMainPane();
        initFileChooser();

        // Init Bindings
        //bindSelectionProperty();

        // Init Scene
        this.scene = new Scene(root, 900,600);

        // Init Resize Policy
        this.setHeights();
        this.resizePolicy();
        this.listenWidth(getScene());
        this.listenDividerPosition(getScene());
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Initialize UI-Items
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void initRootPane() {
        this.root.setTop(getProgramMenuBar());
        this.root.setCenter(getMainPane());
        this.root.setBottom(getStatusBar());
    }

    public void initMainPane() {
        getMainPane().getItems().addAll(getNavigation(),getEditBox());
        getMainPane().setDividerPosition(0, 0.25);
    }

    public void initFileChooser() {
        FileChooser.ExtensionFilter fastaFilter = new FileChooser
                .ExtensionFilter("Fasta Files", "*.fasta", "*.fa");
        this.openFileChooser.setTitle("Open Fasta File");
        this.openFileChooser.getExtensionFilters().add(fastaFilter);
        this.saveFileChooser.setTitle("Save to Fasta File");
        this.saveFileChooser.getExtensionFilters().add(fastaFilter);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Bindings
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void setGcContentProperty(BooleanProperty isUpdated) {
        getEditBox().getGcContent().textProperty().bind(new StringBinding() {
            {
                bind(getNavigation().getSequenceList().getSelectionModel().selectedItemProperty());
                bind(isUpdated);
            }

            @Override
            protected String computeValue() {
                if (!getNavigation().getSequenceList().getSelectionModel().isEmpty())
                    return String.format("GC-Content: " + "%.2f", 100.0 *
                        getNavigation().getSequenceList().getSelectionModel()
                                .getSelectedItem().gcContent());
                return "GC-Content: - ";
            }
        });
    }

    public void setLengthProperty(BooleanProperty isUpdated) {
        getEditBox().getLength().textProperty().bind(new StringBinding() {
            {
                bind(getNavigation().getSequenceList().getSelectionModel().selectedItemProperty());
                bind(isUpdated);
            }
            @Override
            protected String computeValue() {
                if (!getNavigation().getSequenceList().getSelectionModel().isEmpty())
                    return "Length: " + Integer.toString(getNavigation().getSequenceList().getSelectionModel()
                                .getSelectedItem().getLength());
                return "Length: - ";
            }
        });
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Resize Policy
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void setHeights() {
        getEditBox().getSequenceArea().setPrefHeight(getEditAreaHeight());
        getNavigation().getSequenceList().setPrefHeight(
                getSequenceListHeight());
    }

    public void resizePolicy() {
        listenHeight(scene);
    }

    public double getEditAreaHeight() {
        double height = scene.getHeight()
                - getEditBox().getButtonPane().getHeight()
                - getEditBox().getInfoBox().getHeight()
                - getEditBox().getHeaderField().getHeight();
        return height;
    }

    public double getSequenceListHeight() {
        double height = scene.getHeight()
                - getNavigation().getCommandButtons().getHeight();
        return height;
    }

    public void listenHeight(Scene scene) {
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                //int difference = newSceneHeight.intValue()-oldSceneHeight.byteValue();
                setHeights();
            }
        });
    }



    public void listenWidth(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue
                    , Number oldSceneWidth, Number newSceneWidth) {
                int difference = newSceneWidth.intValue()
                        - oldSceneWidth.byteValue();
                getEditBox().computeSliderMax(getMainPane().getDividerPositions()[0]
                        , newSceneWidth.doubleValue());
            }
        });
    }

    public void listenDividerPosition(Scene scene) {
        getMainPane().getDividers().get(0).positionProperty().addListener(
                new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable
                    , Number oldValue, Number newValue) {
                getEditBox().computeSliderMax(newValue.doubleValue()
                        , getScene().getWidth());
            }
        });
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Popup Window
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void showDialog(String dialogText, String dialogHeader) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(dialogHeader);
        VBox dialogBox = new VBox();
        dialogBox.setSpacing(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(15,15,15,15));
        Label dialogLabel = new Label(io.Sequence.toString(dialogText, 60));
        Button ok = new Button("ok");
        ok.setPrefWidth(70);
        ok.setOnAction(value -> {
            dialogStage.close();
        });
        dialogBox.getChildren().addAll(dialogLabel,ok);
        dialogStage.setScene(new Scene(dialogBox));
        dialogStage.show();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Getter and Setter
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public ProgramMenuBar getProgramMenuBar() {
        return programMenuBar;
    }

    public Scene getScene() {
        return scene;
    }

    public FileChooser getSaveFileChooser() {
        return saveFileChooser;
    }

    public FileChooser getOpenFileChooser() {
        return openFileChooser;
    }

    public EditBox getEditBox() {
        return editBox;
    }

    public NavigationBox getNavigation() {
        return navigation;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public SplitPane getMainPane() {
        return mainPane;
    }

    public ObjectProperty<Sequence> sequenceObjectProperty() {
        return sequence;
    }
}
