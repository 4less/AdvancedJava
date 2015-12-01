package simplerna2dviewer;

import io.NucleotideType;
import io.Sequence;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import rna2d.Graph;
import rna2d.Nussinov;
import rna2d.RnaStructureGraph;
import rna2d.SpringEmbedder;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Joachim on 18/11/2015.
 */
public class ViewController {
    private final View view = new View();
    private final Graph graph = new Graph();

    private Sequence sequence;
    private Nussinov nussinov;

    public ViewController() {
        buttonEvents();
    }

    public void show(Stage primaryStage) {
        getView().show(primaryStage);
    }

    public void buttonEvents() {
        getView().getCompute().setOnAction(value -> {
            String sequence = getView().getSequenceField().getText();
            if (!sequence.isEmpty()) {
                try {
                    //valid Sequence
                    this.sequence = new Sequence(sequence, "", NucleotideType.RNA);
                    this.nussinov = new Nussinov(this.sequence.toString());
                    getView().getStructureField().setText(this.nussinov.getBracketNotation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Sequence is empty");
            }
        });
        getView().getDraw().setOnAction(value -> {
            if (!getView().getStructureField().getText().isEmpty()) {
                try {
//                    getGraph().parseNotation(nussinov.getBracketNotation());
                    getGraph().parseNotation(getView().getStructureField().getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double[][] finalcoordinates = SpringEmbedder.computeSpringEmbedding(
                        100,
                        getGraph().getNumberOfNodes(),
                        getGraph().getEdges(),
                        null);
                centerGraph(finalcoordinates);
                getView().getResize().setValue(1.0);
                if (getView().getDrawPane().getChildren().contains(getView().getRnaStructure()))
                    getView().getDrawPane().getChildren().remove(getView().getRnaStructure());
                if(true) {
                    String dummy;
                    if (getView().getSequenceField().getText().isEmpty())
                        dummy = "";
                    else dummy = getView().getSequenceField().getText();
                    if (getView().getAnimate().isSelected()) {
                        double[][] initcoordinates = SpringEmbedder.computeSpringEmbedding(
                                1,
                                getGraph().getNumberOfNodes(),
                                getGraph().getEdges(),
                                null);
                        centerGraph(initcoordinates);

                        getView().setRnaStructure(
                                new RnaStructureGraph(
                                        dummy,
                                        finalcoordinates,
                                        getGraph().getEdges()
                                ));
                        getView().getRnaStructure().show(getView().getDrawPane(), initcoordinates);
                    } else {
                        getView().setRnaStructure(
                                new RnaStructureGraph(
                                        dummy,
                                        finalcoordinates,
                                        getGraph().getEdges()
                                ));
                        getView().getRnaStructure().show(getView().getDrawPane());
                    }
                }

            }
        });
        //Rotation Resize Stuff
        getView().getResize().setOnMouseClicked(value -> updateScaleSlider());
        getView().getResize().setOnMouseDragged(value -> updateScaleSlider());

        getView().getRotate().setOnMouseClicked(value -> updateRotateSlider());
        getView().getRotate().setOnMouseDragged(value -> updateRotateSlider());
    }

    public void updateScaleSlider() {
        getView().getRnaStructure().setScaleX(getView().getResize().getValue());
        getView().getRnaStructure().setScaleY(getView().getResize().getValue());
    }

    public void updateRotateSlider() {
        getView().getRnaStructure().setRotate(getView().getRotate().getValue());
    }

    public void centerGraph(double[][] coordinates) {
        SpringEmbedder.centerCoordinates(
                coordinates,
                getView().getxRange().getStart(),
                getView().getxRange().getEnd(),
                getView().getyRange().getStart(),
                getView().getyRange().getEnd());
    }

    public View getView() {
        return view;
    }

    public Graph getGraph() {
        return graph;
    }
}
