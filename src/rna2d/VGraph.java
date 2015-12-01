package rna2d;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Joachim on 23/11/2015.
 */
public class VGraph extends Group {
    private double[][] nodeCoordinates;
    private int[][] edgeIndices;
    private ObservableList<VNode> nodes = FXCollections.observableArrayList();
    private ObservableList<VEdge> edges = FXCollections.observableArrayList();
    private boolean bidirectional = true;

    public VGraph(double[][] nodeCoordinates, int[][] edgeIndices) {
        loadGraph(nodeCoordinates, edgeIndices);
    }

    public void loadGraph(double[][] nodeCoordinates, int[][] edgeIndices) {
        getNodes().clear();
        getEdges().clear();
        this.getChildren().clear();

        setNodeCoordinates(nodeCoordinates);
        setEdgeIndices(edgeIndices);

        for (int i = 0; i < this.nodeCoordinates.length; i++) {
            if (i == 2) System.out.println(this.nodeCoordinates[i][0] + " : " + this.nodeCoordinates[i][1]);
            VNode vortex = new VNode(i, "", this.nodeCoordinates[i][0], this.nodeCoordinates[i][1]);
            nodes.add(vortex);
            this.getChildren().add(vortex);
        } for (int i = 0; i < this.edgeIndices.length; i++) {
            VNode from = nodes.get(edgeIndices[i][0]);
            VNode to = nodes.get(edgeIndices[i][1]);
            VEdge e = new VEdge(from, to);
            from.add(e);
            edges.add(e);
            getChildren().add(e);
            e.toBack();
            if (bidirectional)
                to.add(e);
        }
    }

    protected void draw(Pane pane) {
        this.setManaged(false);
        this.setAutoSizeChildren(false);
        this.setNeedsLayout(false);
        this.setPickOnBounds(false);
        pane.applyCss();
        pane.layout();
    }

    public void printCoordinates() {
        for (VNode node : getNodes()) {
            System.out.println("Name: " + node.getName() + ", X: " + node.getCenterX() + ", Y: " + node.getCenterY());
        }
    }

    public void setNodeCoordinates(double[][] nodeCoordinates) {
        this.nodeCoordinates = nodeCoordinates;
    }

    public double[][] getNodeCoordinates() {
        return nodeCoordinates;
    }

    public void setEdgeIndices(int[][] edgeIndices) {
        this.edgeIndices = edgeIndices;
    }

    public int[][] getEdgeIndices() {
        return edgeIndices;
    }

    public ObservableList<VNode> getNodes() {
        return nodes;
    }

    public ObservableList<VEdge> getEdges() {
        return edges;
    }
}
