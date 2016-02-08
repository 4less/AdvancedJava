package selection;

import javafx.scene.Node;
import molecules.NucleotideSelectionModel;
import rna2d.VEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 07/02/2016.
 */
public class NucleotideMouseSelection {
    public static void installOnNucleotides(NucleotideSelectionModel selectionModel, List<? extends Node> nodes, int[][] wcbonds) {
        for (int i = 0; i < nodes.size(); i++) {
            final int index = i;
            nodes.get(index).setOnMouseClicked((me) -> {
                System.err.print("is ctrl down: " + me.isControlDown());
                System.err.println("; is alt down: " + me.isAltDown());
                if (me.isControlDown() && !me.isAltDown()) {
                    System.err.println("True");
                    // ADDITIVE SINGLE NUCLEOTIDE SELECTION
                    if (selectionModel.isSelected(index)) {
                        System.err.println("isselected");
                        selectionModel.clearSelection(index);
                    } else {
                        System.err.println("elsecase");
                        selectionModel.select(index);
                    }
                } if (me.isAltDown()) {
                    // HERE STARTS BOND SELECTION BEHAVIOUR
                    final int secondIndex = getWcIndex(index, wcbonds, nodes.size());
                    if (me.isControlDown()) {
                        // CONTROL DOWN ADD SELECTION, ONLY DELETE WHEN BOTH
                        // SELECTED
                        if (selectionModel.isSelected(index)
                                && selectionModel.isSelected(secondIndex)) {
                            selectionModel.clearSelection(index);
                            selectionModel.clearSelection(secondIndex);
                        } else {
                            selectionModel.select(index);
                            selectionModel.select(secondIndex);
                        }
                        // ADDITIVE
                    } else {
                        // CONTROL IS NOT DOWN
                        if (selectionModel.isSelected(index)
                                && selectionModel.isSelected(secondIndex)) {
                            selectionModel.clearSelection();
                        } else {
                            selectionModel.clearSelection();
                            selectionModel.select(index);
                            selectionModel.select(secondIndex);
                        }
                        // NOT ADDITIVE
                    }
                    // END OF BOND SELECTION BEHAVIOUR
                } else if(!me.isControlDown()) {
                    selectionModel.clearAndSelect(index);
                }
            });
        }
    }

    public static void installOnEdges(NucleotideSelectionModel selectionModel, List<VEdge> edges) {
        for (VEdge edge : edges) {
            final int index1 = (int) edge.getFrom().getIndex();
            final int index2 = (int) edge.getTo().getIndex();

            edge.setOnMouseClicked((me) -> {
                if (me.isControlDown()) {
                    if (selectionModel.isSelected(index1)
                            && selectionModel.isSelected(index2)) {
                        selectionModel.clearSelection(index1);
                        selectionModel.clearSelection(index2);
                    } else {
                        selectionModel.select(index1);
                        selectionModel.select(index2);
                    }
                } else if (selectionModel.isSelected(index1)
                        && selectionModel.isSelected(index2)) {
                    selectionModel.clearSelection(index1);
                    selectionModel.clearSelection(index2);
                } else {
                    selectionModel.clearSelection();
                    selectionModel.select(index1);
                    selectionModel.select(index2);
                }
            });
        }
    }

    public static int getWcIndex(int index, int[][] edgeIndices, int size) {
        for (int i = size; i < edgeIndices.length; i++) {
            if (edgeIndices[i][0] == index) {
                return edgeIndices[i][1];
            } if (edgeIndices[i][1] == index) {
                return edgeIndices[i][0];
            }
        }
        return -1;
    }
}
