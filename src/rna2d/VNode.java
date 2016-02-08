package rna2d;

import dnamanipulator.view.SelectionAlert;
import io.NucleotideType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import rna1d.BaseType;
import selection.Selectable;

/**
 * Created by Joachim on 23/11/2015.
 */
public class VNode extends Circle implements Selectable {
    private String name;
    private long index;
    private ObservableList<VEdge> edges = FXCollections.observableArrayList();
    private Tooltip tooltip;
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private static final double RADIUS = 3;

    public VNode(long index, String name, double x, double y) {
        this.setCenterX(x);
        this.setCenterY(y);

        this.name = name;
        this.index = index;
        this.tooltip = new Tooltip(index + " : " + name);
        this.setRadius(RADIUS);
        Tooltip.install(this, tooltip);

        setSelection();
    }

    public void add(VEdge e) {
        this.edges.add(e);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.tooltip.setText(this.tooltip.getText().substring(0,this.tooltip.getText().indexOf(':')) + ": " + name + System.lineSeparator() +
            "X: " + getCenterX() + "; Y: " + getCenterY());
    }

    public void setSelection() {
        this.setStroke(Color.BLACK);
        this.radiusProperty().bind(Bindings.when(selected)
                .then(this.getRadius() * 1.5)
                .otherwise(this.getRadius()));
        this.strokeWidthProperty().bind(Bindings.when(selected)
                .then(3)
                .otherwise(0));
    }

    public long getIndex() {
        return index;
    }

    public ObservableList<VEdge> getEdges() {
        return edges;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
