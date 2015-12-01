package rna2d;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Created by Joachim on 23/11/2015.
 */
public class RnaStructureGraph extends VGraph {
    private char[] rna;

    public RnaStructureGraph(String sequence, double[][] nodeCoordinates, int[][] edgeIndices) {
        super(nodeCoordinates, edgeIndices);
        this.rna = sequence.toCharArray();
        setNames();
        colorNucleotides();
        colorEdges();

//        Rectangle base = new Rectangle(20000,20000);
//        base.setFill(Color.WHITE);
//        this.getChildren().add(base);
//        base.toBack();
    }

    public void show(Pane pane) {
        super.draw(pane);
        setNodeSelection();
        pane.getChildren().add(this);
    }

    public void setNames() {
        if (rna.length == getNodes().size()) {
            for (int i = 0; i < rna.length; i++) {
                getNodes().get(i).setName(String.valueOf(rna[i]));
            }
        }
    }

    public void show(Pane pane, double[][] initialCoordinates) {
        show(pane);

        for (VNode node : getNodes()) {
            node.setLayoutX(initialCoordinates[(int) node.getIndex()][0] - node.getCenterX());
            node.setLayoutY(initialCoordinates[(int) node.getIndex()][1] - node.getCenterY());
            for (VEdge edge : node.getEdges()) {
                edge.update();
            }
        }

        if (initialCoordinates.length == getNodeCoordinates().length) {
            Timeline transition = new Timeline();
            transition.setAutoReverse(false);
            transition.setCycleCount(1);

            for (VNode node : getNodes()) {
                transiteToCenter(transition, node);
            }

            transition.play();
        }
    }

    public void colorNucleotides() {
        for (VNode nucleotide : this.getNodes()) {
            if(!nucleotide.getName().isEmpty())
                nucleotide.setFill(color(nucleotide.getName().charAt(0)));
        }
    }

    private static Paint color(char c) {
        switch(c) {
            case 'A': return Paint.valueOf("Green");
            case 'C': return Paint.valueOf("Red");
            case 'G': return Paint.valueOf("Orange");
            case 'U': return Paint.valueOf("Blue");
        }
        return Paint.valueOf("Black");
    }

    private boolean areNeighbors(VNode one, VNode two) {
        return Math.pow(one.getIndex(),two.getIndex()) != 1;
    }

    public void colorEdges() {
        for (VEdge bond : this.getEdges()) {
            if (areNeighbors(bond.getFrom(), bond.getTo()))
                bond.setFill(Paint.valueOf("silver"));
        }
    }

    private void transiteToCenter(Timeline timeline, VNode nucleotide) {
        for (VEdge vedge : nucleotide.getEdges()) {
            double x = nucleotide.getCenterX();
            double y = nucleotide.getCenterY();
            KeyFrame keyFrameEdge = null;
            if (vedge.isStart(nucleotide))
                keyFrameEdge = new KeyFrame(Duration.millis(2000),
                        new KeyValue(vedge.startXProperty(), x),
                        new KeyValue(vedge.startYProperty(), y));
            else if (vedge.isEnd(nucleotide))
                keyFrameEdge = new KeyFrame(Duration.millis(2000),
                        new KeyValue(vedge.endXProperty(), x),
                        new KeyValue(vedge.endYProperty(), y));
            timeline.getKeyFrames().add(keyFrameEdge);
        }
        KeyFrame keyFrameNode = new KeyFrame(Duration.millis(2000),
                new KeyValue(nucleotide.layoutXProperty(), 0.0),
                new KeyValue(nucleotide.layoutYProperty(), 0.0));
        timeline.getKeyFrames().add(keyFrameNode);
    }

    private void setNodeSelection() {
        class Delta { double x, y; }

        for (VNode nucleotide : this.getNodes()) {
            final Delta dragDelta = new Delta();
            nucleotide.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = nucleotide.getLayoutX() - mouseEvent.getSceneX();
                    dragDelta.y = nucleotide.getLayoutY() - mouseEvent.getSceneY();
                    nucleotide.setCursor(Cursor.MOVE);
                }
            });
            nucleotide.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    nucleotide.setCursor(Cursor.HAND);

                    Timeline transition = new Timeline();
                    transition.setCycleCount(1);
                    transition.setAutoReverse(false);
                    transiteToCenter(transition, nucleotide);
                    transition.play();
                }
            });
            nucleotide.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    nucleotide.setLayoutX((mouseEvent.getSceneX() + dragDelta.x)/getScaleX());
                    nucleotide.setLayoutY((mouseEvent.getSceneY() + dragDelta.y)/getScaleY());
                    System.out.println(dragDelta.x + " : " + dragDelta.y);
                    for (VEdge vedge : nucleotide.getEdges())
                        vedge.update();
                }
            });
            nucleotide.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    nucleotide.setCursor(Cursor.HAND);
                }
            });
        }
    }

    public void printCoordinates(Circle n) {
        System.out.println("Layout: " + n.getLayoutX() + " : " + n.getLayoutY());
        System.out.println("Center: " + n.getCenterX() + " : " + n.getCenterY());
        System.out.println("Transl: " + n.getTranslateX() + " : " + n.getTranslateY());
        System.out.println("ScaleX: " + getScaleX() + " : ScaleY: " + getScaleY());
        System.out.println("LayoutGroup: " + getLayoutBounds().getWidth() + " : " + getLayoutBounds().getHeight());
    }
}
