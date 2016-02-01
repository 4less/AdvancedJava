package rna1d;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


/**
 * Created by Jogi on 21.01.2016.
 */
public class BaseBox extends VBox {
    private BaseType base;
    private final Label baseLabel = new Label("");
    //private final Line baseType =
    private final Label basePosition = new Label("");
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public BaseBox(BaseType base, int position) {
        this.base = base;

        //Style
        baseLabel.getStylesheets().add("rna1d/primary.css");
        colorNucleotides();

        baseLabel.setText(String.valueOf(base.getBase()));
        basePosition.setText(String.valueOf(position));
        basePosition.setRotate(90.0);
        basePosition.setPrefHeight(25);
        this.getChildren().addAll(baseLabel, basePosition);
        setSelection();
    }

    public void setSelection() {
        this.backgroundProperty().bind(Bindings.when(selected)
            .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
            .otherwise(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public void colorNucleotides() {
        baseLabel.setBackground(new Background(new BackgroundFill(this.base.getBaseColor(), new CornerRadii(4.0), new Insets(1.0,1.0,1.0,1.0))));
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public BaseType getBase() {
        return base;
    }
}
