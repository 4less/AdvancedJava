package simplerna2dviewer;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rna2d.RnaStructureGraph;

/**
 * Created by Joachim on 18/11/2015.
 */
public class View {
    private final Scene scene;
    private final BorderPane root = new BorderPane();
    private final Pane drawPane = new Pane();

    private RnaStructureGraph rnaStructure;

    private int margin = 10;
    private IndexRange xRange;
    private IndexRange yRange;

    private VBox topBox = new VBox();

    private final TextField sequenceField = new TextField();
    private final TextField structureField = new TextField();

    private final HBox itemBox = new HBox();
    private final Button compute = new Button("Compute");
    private final Button draw = new Button("Draw");
    private final Slider rotate = new Slider(-160.0,160.0,0.0);
    private final Slider resize = new Slider(0.2,5.0,1.0);

    private final CheckBox animate = new CheckBox("animate");

    public View() {
        initPanes();

        scene = new Scene(root, 900,600);

        root.applyCss();
        root.layout();

        setXrange();
        setYrange();
    }

    public void show(Stage primaryStage) {
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple RNA 2D viewer");
        primaryStage.show();
    }

    public void setXrange() {
        this.xRange = new IndexRange(this.margin,(int) Math.round(this.scene.getWidth()-this.margin));
    }

    public void setYrange() {
        System.out.println("Height: " + this.scene.getHeight());
        System.out.println("Height Sequencefield: " + this.getSequenceField().getHeight());
        System.out.println("Height itembox: " + this.topBox.getHeight());
        this.yRange = new IndexRange((int) Math.round(this.margin+this.topBox.getHeight()),(int) Math.round(this.scene.getHeight()-this.margin));
    }

    public void initPanes() {
        Font font = Font.font(java.awt.Font.MONOSPACED, 12);
        getSequenceField().setFont(font);
        getStructureField().setFont(font);
        getStructureField().setEditable(true);

        resize.setPrefWidth(100);
        resize.setMajorTickUnit(1.0);
        resize.setShowTickMarks(true);

        rotate.setPrefWidth(100);
        rotate.setMajorTickUnit(20);
        rotate.setShowTickMarks(true);

        itemBox.getChildren().addAll(compute,draw,rotate,animate);
        topBox.getChildren().addAll(sequenceField, structureField, itemBox);
        root.setTop(topBox);
        root.setCenter(drawPane);

        //test
        drawPane.toBack();
        topBox.toFront();


//        root.getChildren().addAll(sequenceField, structureField, drawPane);
//        drawPane.getChildren().addAll(itemBox);
    }

    public CheckBox getAnimate() {
        return animate;
    }

    public Button getDraw() {
        return draw;
    }

    public Button getCompute() {
        return compute;
    }

    public Slider getResize() {
        return resize;
    }

    public TextField getStructureField() {
        return structureField;
    }

    public TextField getSequenceField() {
        return sequenceField;
    }

    public Pane getDrawPane() {
        return drawPane;
    }

    public RnaStructureGraph getRnaStructure() {
        return rnaStructure;
    }

    public void setRnaStructure(RnaStructureGraph rnaStructure) {
        this.rnaStructure = rnaStructure;
    }

    public IndexRange getxRange() {
        return xRange;
    }

    public IndexRange getyRange() {
        return yRange;
    }

    public Slider getRotate() {
        return rotate;
    }

}
