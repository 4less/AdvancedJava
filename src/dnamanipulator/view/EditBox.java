package dnamanipulator.view;

import io.Sequence;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Created by Joachim on 11/11/2015.
 */
public class EditBox extends VBox {
    private TextField headerField = new TextField();
    private TextArea sequenceArea = new TextArea();
    private HBox infoBox = new HBox();

    private Label length = new Label("Length:");
    private Label gcContent = new Label("GC Content:");

    private FlowPane buttonPane = new FlowPane();

    private Button filter = new Button("Filter");
    private Button upperCase = new Button("to Upper Case");
    private Button lowerCase = new Button("to Lower Case");
    private Button convert = new Button("Convert to");
    private Button reverse =
            new Button("Reverse");
    private Button complementary =
            new Button("Complementary");
    private Button reverseComplementary =
            new Button("reverseComplementary");

    private Slider slider = new Slider();

    public EditBox() {
        super();
        initInfoBox();
        initButtonPane();
        initSlider();

        this.setAlignment(Pos.CENTER);

        Font font = Font.font("Monospaced", 12);
        getHeaderField().setFont(font);
        getSequenceArea().setFont(font);

        getButtonPane().setPrefWidth(Double.MAX_VALUE);
        getButtonPane().setAlignment(Pos.CENTER);
        this.setPrefWidth(Double.MAX_VALUE);

        getSequenceArea().setPadding(new Insets(3,3,3,3));
        getHeaderField().setPadding(new Insets(2,2,2,2));

        this.getChildren().addAll(headerField, sequenceArea, infoBox
                , buttonPane, slider);
    }

    public void initSlider() {
        this.slider.setShowTickMarks(true);
        this.slider.setShowTickLabels(true);
        this.slider.setMajorTickUnit(20);
        this.slider.setMaxWidth(400);
        this.slider.setValue(88);

        this.slider.setOnMouseDragged(value -> updateSlider());
        this.slider.setOnMouseClicked(value -> updateSlider());
    }

    public void initInfoBox() {
        this.infoBox.setSpacing(10);
        this.infoBox.setPadding(new Insets(3,15,3,15));
        this.infoBox.getChildren().addAll(getLength(),getGcContent());
    }

    public void initButtonPane() {
        getButtonPane().setPrefWidth(Double.MAX_VALUE);
        getButtonPane().setHgap(5);
        getButtonPane().setVgap(5);
        getButtonPane().setPadding(new Insets(5, 5, 5, 5));

        getButtonPane().getChildren().addAll(getConvert(), getFilter(), getLowerCase()
                , getUpperCase(), getReverse(), getComplementary()
                , getReverseComplementary());
    }

    public void textWrap() {
        if (slider.getValue() >= 0) {
            getSequenceArea().setText(Sequence.toString(
                    getSequenceArea().getText().replaceAll("\\n", "")
                    , (int) Math.round(slider.getValue())));
        }
    }

    public void computeSliderMax(double dividerPos, double sceneWidth) {
        getSlider().setMax(Math.round(
                (1 - dividerPos)
                        * sceneWidth/6));

    }

    public void bindDisableButtonProperty(BooleanProperty isFile, BooleanProperty isUpdated, BooleanProperty isSelected) {
        getFilter().disableProperty().bind((isFile.and(isSelected)).not());
        getReverse().disableProperty().bind((isFile.and(isUpdated).and(isSelected)).not());
        getComplementary().disableProperty().bind((isFile.and(isUpdated).and(isSelected)).not());
        getReverseComplementary().disableProperty().bind((isFile.and(isUpdated).and(isSelected)).not());
        getSlider().disableProperty().bind(isFile.not());
        getConvert().disableProperty().bind(isFile.not());
        getLowerCase().disableProperty().bind((isFile.and(isUpdated).and(isSelected)).not());
        getUpperCase().disableProperty().bind((isFile.and(isUpdated).and(isSelected)).not());
        getSequenceArea().disableProperty().bind((isFile.and(isSelected)).not());
        getHeaderField().disableProperty().bind((isFile.and(isSelected)).not());
    }

    public void updateSlider() {
        textWrap();
    }

    public TextField getHeaderField() {
        return headerField;
    }

    public TextArea getSequenceArea() {
        return sequenceArea;
    }

    public HBox getInfoBox() {
        return infoBox;
    }

    public Label getLength() {
        return length;
    }

    public Label getGcContent() {
        return gcContent;
    }

    public FlowPane getButtonPane() {
        return buttonPane;
    }

    public Button getReverse() {
        return reverse;
    }

    public Button getComplementary() {
        return complementary;
    }

    public Button getReverseComplementary() {
        return reverseComplementary;
    }

    public Slider getSlider() {
        return slider;
    }

    public Button getConvert() {
        return convert;
    }

    public Button getLowerCase() {
        return lowerCase;
    }

    public Button getUpperCase() {
        return upperCase;
    }

    public Button getFilter() {
        return filter;
    }

    public String getUnformatedText() {
        return getSequenceArea().getText().replaceAll("\\n", "");
    }
}
