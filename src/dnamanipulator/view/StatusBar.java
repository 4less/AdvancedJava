package dnamanipulator.view;

import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Joachim on 11/11/2015.
 */
public class StatusBar extends HBox {
    private Label sequenceCount = new Label("0");
    private Label nucleoType = new Label("-");

    public StatusBar() {
        super();
        this.setSpacing(15);
        this.setPadding(new Insets(2,2,2,2));
        this.getChildren().addAll(nucleoType, sequenceCount);
    }


    public Label getSequenceCount() {
        return sequenceCount;
    }

    public Label getNucleoType() {
        return nucleoType;
    }

    public void setNucleoType(String nucleoType) {
        this.nucleoType.setText(nucleoType);
    }

    public void setSequenceCount(String sequenceCount) {
        this.sequenceCount.setText(sequenceCount);
    }
}
