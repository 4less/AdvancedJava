package program;

import io.Sequence;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by Joachim on 28/10/2015.
 */
public class DNAManipulator extends Application {
    final private VBox root = new VBox();
    final private TextArea editArea = new TextArea();
    final private Button flip = new Button("flip");
    final private TextArea showArea = new TextArea();
    final private TilePane tPain =  new TilePane();
    private HashMap<String, Button> buttonMap = new HashMap<String, Button>();
    private Controller controller;
    final private Slider slider = new Slider(20,100,60);

    /**
     * Initialize all UI objects
     * Initialize the Controller
     * @throws Exception
     */
    public void init() throws Exception {
        System.err.println("init()");

        // Init root pane
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10,10,10,10));

        // Init tPain
        tPain.setAlignment(Pos.CENTER);
        tPain.setPadding(new Insets(10,10,10,10));
        tPain.setVgap(4);
        tPain.setHgap(4);

        // Bottom TextArea set to not editable
        showArea.setEditable(false);
        Font font = Font.font("Monospaced", 12);
        editArea.setFont(font);
        showArea.setFont(font);

        // init Buttons
        initButtons();

        // init Slider
        this.slider.setShowTickMarks(true);
        this.slider.setShowTickLabels(true);
        this.slider.setMajorTickUnit(20);
        this.slider.setMaxWidth(400);

        // Add all to root Pane
        root.getChildren().addAll(this.editArea,this.flip,this.showArea,this.tPain,this.slider);

        // init Controller
        this.controller = new Controller();

        super.init();
    }

    /**
     *  Adjust TextArea height dynamically to the windows size
     * @param scene
     */
    public void listenHeight(Scene scene) {
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                int difference = newSceneHeight.intValue()-oldSceneHeight.byteValue();
                showArea.setPrefHeight(showArea.getHeight()+difference/2);
                editArea.setPrefHeight(editArea.getHeight()+difference/2);
            }
        });
    }

    /**
     *  Adjust slider max value dynamically to the windows size
     * @param scene
     */
    public void listenWidth(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                double difference = newSceneWidth.intValue()-oldSceneWidth.intValue();
                slider.setMax(Math.round(slider.getMax() + difference/5));
            }
        });
    }


    /**
     * Updates showArea with the latest Sequence from the controller
     */
    public void updateShowArea() {
        this.showArea.setText(this.controller.getSequence().toString());
    }

    /**
     * Does aaaaaall the button magic with the magic buttons!
     */
    public void buttonMagic() {
        this.flip.setOnAction(value -> {
            String cache = this.showArea.getText();
            this.showArea.setText(this.editArea.getText());
            this.editArea.setText(cache);
        });
        this.buttonMap.get("filter").setOnAction(value -> this.modify(Function.identity()));
        this.buttonMap.get("upper case").setOnAction(value -> this.modify(Sequence::toUpperCase));
        this.buttonMap.get("lower case").setOnAction(value -> this.modify(Sequence::toLowerCase));
        this.buttonMap.get("to RNA").setOnAction(value -> this.modify(Sequence::toRNA));
        this.buttonMap.get("reverse").setOnAction(value -> this.modify(Sequence::reverse));
        this.buttonMap.get("complementary").setOnAction(value -> this.modify(Sequence::complement));
        this.buttonMap.get("reverse complementary").setOnAction(value -> this.modify(Sequence::reverseComplementary));
        this.buttonMap.get("length").setOnAction(value -> {
            this.controller.filter(this.editArea.getText());
            this.showArea.setText(Integer.toString(this.controller.getSequence().getLength()));
        });
        this.buttonMap.get("GC content").setOnAction(value -> {
            this.controller.filter(this.editArea.getText());
            this.showArea.setText(Double.toString(this.controller.getSequence().gcContent()*100) + "%");
        });
        this.buttonMap.get("clear").setOnAction(value -> {
            this.showArea.clear();
            this.editArea.clear();
            this.controller.getSequence().clear();
        });
    }

    /**
     * Function modify to modify showArea's content easily
     * @param f
     */
    public void modify(Function<Sequence,Sequence> f) {
        this.controller.filter(this.editArea.getText());
        f.apply(this.controller.getSequence());
        this.updateShowArea();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Set scene
        Scene scene = new Scene(root,500,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DNA Manipulator 3000");

        buttonMagic();
        listenHeight(scene);
        listenWidth(scene);

        this.slider.setOnMouseDragged(value -> updateSlider());
        this.slider.setOnMouseClicked(value -> updateSlider());
        primaryStage.show();
    }

    /**
     * Update test with value of Slider
     */
    public void updateSlider() {
        this.showArea.setText(Sequence.toString(this.showArea.getText().replaceAll("\\n",""), (int) slider.getValue()));
    }

    /**
     * Function which creates new Buttons and directly adds them to a hashmap
     * @param label Button text and key in Hashmap
     * @return
     */
    public Button newButton(String label) {
        Button button = new Button(label);
        button.setMaxWidth(Double.MAX_VALUE);
        this.buttonMap.put(label,button);
        return button;
    }

    /**
     * Initialize Buttons and add them to tPain
     */
    public void initButtons() {
        tPain.getChildren().addAll(newButton("filter"),
            newButton("upper case"),
            newButton("lower case"),
            newButton("to RNA"),
            newButton("reverse"),
            newButton("complementary"),
            newButton("reverse complementary"),
            newButton("GC content"),
            newButton("length"),
            newButton("clear"));
    }
}
