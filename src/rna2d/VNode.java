package rna2d;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;

/**
 * Created by Joachim on 23/11/2015.
 */
public class VNode extends Circle {
    private String name;
    private long index;
    private ObservableList<VEdge> edges = FXCollections.observableArrayList();
    private Tooltip tooltip;

    public VNode(long index, String name, double x, double y) {
        this.setCenterX(x);
        this.setCenterY(y);

        this.name = name;
        this.index = index;
        this.tooltip = new Tooltip(index + " : " + name);
        this.setRadius(5);
        Tooltip.install(this, tooltip);
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

    public long getIndex() {
        return index;
    }

    public ObservableList<VEdge> getEdges() {
        return edges;
    }
}
