package rnaxd;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point3D;
import rna1d.BaseType;
import rna2d.RnaStructureGraph;
import rna2d.SpringEmbedder;

/**
 * Created by Joachim on 06/02/2016.
 */
public class View2DController {
    private RnaStructureGraph graph2d;
    private static final int MARGIN_2D = 20;

    private ChangeListener<Number> heightChangeListener;
    private ChangeListener<Number> widthChangeListener;

    private DoubleBinding scaleBinding;

    private BoundChange bounds;

    private class BoundChange {
        double xNew = 1;
        double xOld = 1;
        double yNew = 1;
        double yOld = 1;
        double scale = 1;
    }

    public void initGraph2d(String sequence, int size, int[][] edges, int width, int height) {
        double[][] initcoordinates = SpringEmbedder.computeSpringEmbedding(
                1,
                size,
                edges,
                null);

        double[][] finalcoordinates = SpringEmbedder.computeSpringEmbedding(
                300,
                size,
                edges,
                null);

        centerGraph(initcoordinates, width, height);
        centerGraph(finalcoordinates, width, height);

        graph2d = new RnaStructureGraph(sequence, finalcoordinates, edges, BaseType::getBaseColorFromString);
        graph2d.setInitialCoordinates(initcoordinates);
    }

    public void centerGraph(double[][] coordinates, int width, int height) {
        SpringEmbedder.centerCoordinates(
                coordinates,
                0 + MARGIN_2D,
                width - MARGIN_2D,
                0 + MARGIN_2D,
                height - MARGIN_2D);
    }

    public RnaStructureGraph getGraph2d() {
        return graph2d;
    }

    public void translateGraphBy(Point3D by) {
        graph2d.setTranslateX(by.getX());
        graph2d.setTranslateY(by.getY());
        graph2d.setTranslateZ(by.getZ());
    }

    public void initProperties() {
        widthChangeListener = (observable, oldValue, newValue) -> {
            bounds.xNew = (double) newValue;
            bounds.xOld = (double) oldValue;
            bounds.scale = getGraph2d().getScaleX();
            double delta = (double) newValue - (double) oldValue;
            getGraph2d().setLayoutX(getGraph2d().getLayoutX() + delta/2);
        };
        heightChangeListener = (observable, oldValue, newValue) -> {
            bounds.yNew = (double) newValue;
            bounds.yOld = (double) oldValue;
            bounds.scale = getGraph2d().getScaleX();
            double delta = (double) newValue - (double) oldValue;
            getGraph2d().setLayoutY(getGraph2d().getLayoutY() + delta/2);
        };
    }

    public ChangeListener<Number> getHeightChangeListener() {
        return heightChangeListener;
    }

    public ChangeListener<Number> getWidthChangeListener() {
        return widthChangeListener;
    }

    public void setScaleBinding(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty) {
        bounds = new BoundChange();
        this.scaleBinding = new DoubleBinding() {
            {
                bind(widthProperty);
                bind(heightProperty);
            }

            @Override
            protected double computeValue() {
                double heightScaling = (bounds.xNew / bounds.xOld);
                double widthScaling = (bounds.yNew / bounds.yOld);
                System.err.println("###############################");
                if (getGraph2d() != null) {
                    System.err.println("graph is not null");
                    //System.err.println(getGraph2d().getScaleX());
                }
                //System.err.println("Actual Scaling: " + getGraph2d().scaleXProperty().get());
                System.err.println(bounds.xNew + " / " + bounds.xOld + " = " + widthScaling + " * " + bounds.scale + " = " + widthScaling*bounds.scale);
                System.err.println(bounds.yNew + " / " + bounds.yOld + " = " + heightScaling + " * " + bounds.scale + " = " + heightScaling*bounds.scale);
                System.err.println(Math.min(heightScaling*bounds.scale, widthScaling*bounds.scale));
                bounds.scale = (Math.min(heightScaling,widthScaling)*bounds.scale);
                return Math.min(heightScaling, widthScaling);
            }
        };
        getGraph2d().scaleXProperty().bind(scaleBinding);
        getGraph2d().scaleYProperty().bind(scaleBinding);
    }
}
