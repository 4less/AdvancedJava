package rna2d;

import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.scene.shape.Line;

import java.awt.event.MouseEvent;

/**
 * Created by Joachim on 23/11/2015.
 */
public class VEdge extends Line {
    private VNode from;
    private VNode to;
    private double weight;

    public VEdge(VNode from, VNode to) {
        this.from = from;
        this.to = to;
        this.setStrokeWidth(2);

        this.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                update();
            }
        });

        this.setStartX(from.getCenterX());
        this.setStartY(from.getCenterY());
        this.setEndX(to.getCenterX());
        this.setEndY(to.getCenterY());

//        this.startXProperty().add(new DoubleBinding() {
//            {
//                bind(from.centerXProperty());
//                bind(from.layoutXProperty());
//            }
//            @Override
//            protected double computeValue() {
//                System.err.println("Freakshow");
//                return from.getCenterX()+from.getLayoutX();
//            }
//        });
//        this.startYProperty().add(new DoubleBinding() {
//            {
//                bind(from.centerYProperty());
//                bind(from.layoutYProperty());
//            }
//            @Override
//            protected double computeValue() {
//                return from.getCenterY()+from.getLayoutY();
//            }
//        });
//        this.endXProperty().add(new DoubleBinding() {
//            {
//                bind(to.centerXProperty());
//                bind(to.layoutXProperty());
//            }
//            @Override
//            protected double computeValue() {
//                return to.getCenterX()+to.getLayoutX();
//            }
//        });
//        this.endYProperty().add(new DoubleBinding() {
//            {
//                bind(to.centerYProperty());
//                bind(to.layoutYProperty());
//            }
//            @Override
//            protected double computeValue() {
//                return to.getCenterY()+to.getLayoutY();
//            }
//        });

//        this.startXProperty().bind(from.layoutXProperty().add(from.translateXProperty()));
//        this.startYProperty().bind(from.layoutYProperty().add(from.translateYProperty()));
//        this.endXProperty().bind(to.layoutXProperty().add(to.translateXProperty()));
//        this.endYProperty().bind(to.layoutYProperty().add(translateYProperty()));

//        this.startXProperty().bind(from.centerXProperty().add(from.translateXProperty()));
//        this.startYProperty().bind(from.centerYProperty().add(from.translateYProperty()));
//        this.endXProperty().bind(to.centerXProperty().add(to.translateXProperty()));
//        this.endYProperty().bind(to.centerYProperty().add(to.translateYProperty()));
    }

    public VEdge(VNode from, VNode to, double weight) {
        new VEdge(from,to);
        this.weight = weight;
    }

    public void reset() {
        setStartX(from.getCenterX());
        setStartY(from.getCenterY());
        setEndX(to.getCenterX());
        setEndY(to.getCenterY());
    }

    public void update() {
        setStartX(from.getCenterX() + from.getLayoutX());
        setStartY(from.getCenterY() + from.getLayoutY());
        setEndX(to.getCenterX() + to.getLayoutX());
        setEndY(to.getCenterY() + to.getLayoutY());
    }

    public boolean isStart(VNode node) {
        return node == from;
    }

    public boolean isEnd(VNode node) {
        return node == to;
    }

    public VNode getFrom() {
        return from;
    }

    public VNode getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }
}
