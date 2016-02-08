package rnaxd;

import javafx.beans.binding.DoubleBinding;
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

    private ChangeListener<Number> widthChangeListener;
    private ChangeListener<Number> heightChangeListener;
    private DoubleBinding scaleBinding = new DoubleBinding() {
        {
            bind(model.getCoordinatesBoundsObservable());
            bind(objectsPane.widthProperty());
            bind(objectsPane.heightProperty());
        }

        @Override
            protected double computeValue() {
            double heightScaling = objectsPane.getHeight() / model.getCoordinatesBoundsObservable().getValue().getHeight();
            double widthScaling = objectsPane.getWidth() / model.getCoordinatesBoundsObservable().getValue().getWidth();
            return Math.min(heightScaling, widthScaling);
        }
    };


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
            double delta = (double) newValue - (double) oldValue;
            getGraph2d().setLayoutX(getGraph2d().getLayoutX() + delta/2);
        };
        heightChangeListener = (observable, oldValue, newValue) -> {
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
}
