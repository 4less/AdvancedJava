package rna2d;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.util.Duration;

/**
 * Created by Joachim on 18/11/2015.
 */
public class Rna2dStructure extends Group {
    private double[][] coordinates;
    private int[][] edgecoordinates;
    private ObservableList<Circle> nucleotides = FXCollections.observableArrayList();
    private ObservableList<Line> edges = FXCollections.observableArrayList();
    private DoubleProperty radius = new SimpleDoubleProperty(3.0);

    double originx = 0.0;
    double originy = 0.0;


    public void colorNucleotides(String sequence) {
        char[] base = sequence.toCharArray();
        if (base.length == nucleotides.size()) {
            for (int i = 0; i < base.length; i++) {
                nucleotides.get(i).toFront();
                Tooltip t = new Tooltip(i + " : " + String.valueOf(base[i]));
                Tooltip.install(nucleotides.get(i), t);

                switch(base[i]) {
                    case 'A':
                        nucleotides.get(i).setFill(Paint.valueOf("green"));
                        break;
                    case 'C':
                        nucleotides.get(i).setFill(Paint.valueOf("orange"));
                        break;
                    case 'G':
                        nucleotides.get(i).setFill(Paint.valueOf("red"));
                        break;
                    case 'U':
                        nucleotides.get(i).setFill(Paint.valueOf("blue"));
                        break;
                }
            }
        }

    }

    public void drawAnimate(double[][] to, Pane pane) {
        drawCircles();
        pane.applyCss();
        pane.layout();
        constructEdges();
        drawPath(pane);
        colorNucleotides("GGGAAGAUAUAAUCCUAAUGAUAUGGUUUGGGAGUUUCUACCAAGAGCCUUAAACUCUUGAUUAUCUUCCCA");

        Timeline transition = new Timeline();
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        double x;
        double y;
        for (int i = 0; i < coordinates.length; i++) {
            x = to[i][0]-coordinates[i][0];
            y = to[i][1]-coordinates[i][1];
            transition.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(nucleotides.get(i).translateXProperty(), x)));
            transition.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(nucleotides.get(i).translateYProperty(), y)));
        }
        for (int i = 0; i < edgecoordinates.length; i++) {
        }
        transition.play();
    }

    public void selectable() {

        this.nucleotides.get(1).setOnMouseDragged(value -> {
            originx = nucleotides.get(1).getCenterX();
            originy = nucleotides.get(1).getCenterY();

            double x = value.getX();
            double y = value.getY();

            //nucleotides.get(1).relocate(x,y);
            nucleotides.get(1).setCenterX(x);
            nucleotides.get(1).setCenterY(y);

            if (true) {
                edges.get(0).setEndX(x+100.0);
                edges.get(0).setEndY(y+100.0);
                edges.get(0).setStartX(x);
                edges.get(0).setStartY(y);
                edges.get(0).toBack();
                edges.get(0).setStrokeWidth(20.0);
            }
            if (1 < edges.size()) {
                edges.get(0).setStartX(x);
                edges.get(0).setStartY(y);
            }
        });

        this.nucleotides.get(1).setOnMouseReleased(value -> {
            double x = originx;
            double y = originy;

            nucleotides.get(1).setCenterX(x);
            nucleotides.get(1).setCenterY(y);

            if (1-1 >= 0) {
                edges.get(0).setEndX(x);
                edges.get(0).setEndY(y);
            }
            if (1 < edges.size()) {
                edges.get(0).setStartX(x);
                edges.get(0).setStartY(y);
            }
        });
    }

    public void setNucleotides(double[][] coordinates, int[][] edgecoordinates) {
        this.coordinates = coordinates;
        this.edgecoordinates = edgecoordinates;
    }

    public void drawCircles() {
        for (int i = 0; i < coordinates.length; i++) {
            Circle node = new Circle(coordinates[i][0], coordinates[i][1], radius.get());
            node.radiusProperty().bind(radius);
            nucleotides.add(node);
            this.getChildren().addAll(node);
        }
    }

    public void constructEdges() {
        for (int i = 0; i < edgecoordinates.length; i++) {
            int from = edgecoordinates[i][0];
            int to = edgecoordinates[i][1];
            Circle circle1 = nucleotides.get(from);
            Circle circle2 = nucleotides.get(to);
            Line edge = new Line(circle1.getCenterX(), circle1.getCenterY(), circle2.getCenterX(), circle2.getCenterY());

            edge.startXProperty().bind(circle1.centerXProperty().add(circle1.translateXProperty()));
            edge.startYProperty().bind(circle1.centerYProperty().add(circle1.translateYProperty()));
            edge.endXProperty().bind(circle2.centerXProperty().add(circle2.translateXProperty()));
            edge.endYProperty().bind(circle2.centerYProperty().add(circle2.translateYProperty()));



            if (Math.pow((from - to), 2) != 1) {
                edge.setStroke(Paint.valueOf("AQUA"));
            }
            this.edges.add(edge);
            this.getChildren().add(edge);
        }
    }

    public void drawPath(Pane pane) {
        //selectable();
        pane.getChildren().add(this);
        this.toBack();
    }

    public void center(int xMin, int xMax, int yMin, int yMax) {
        SpringEmbedder.centerCoordinates(this.coordinates, xMin, xMax, yMin, yMax);
    }

    public void printCoordinates() {
        for (double[] coordinate : this.coordinates) {
            System.out.println("X: " + coordinate[0] + "Y: " + coordinate[1]);
        }
    }
}
