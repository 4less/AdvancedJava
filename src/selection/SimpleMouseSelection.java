package selection;

import javafx.scene.Node;
import javafx.scene.control.MultipleSelectionModel;

import java.util.ArrayList;

/**
 * Created by Joachim on 07/02/2016.
 */
public class SimpleMouseSelection {
    public static void install(MultipleSelectionModel selectionModel, ArrayList<Node> nodes) {
        install(selectionModel, nodes.toArray(new Node[nodes.size()]));
    }

    public static void install(MultipleSelectionModel selectionModel, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            final int index = i;
            nodes[index].setOnMouseClicked((me) -> {
                if (me.isControlDown()) {
                    if (selectionModel.isSelected(index))
                        selectionModel.clearSelection(index);
                    else
                        selectionModel.select(index);
                } else if (selectionModel.isSelected(index)) {
                    selectionModel.clearSelection(index);
                } else {
                    selectionModel.clearSelection();
                    selectionModel.select(index);
                }
            });
        }
    }

    public static void remove(Node... nodes) {
        for (Node node : nodes) {
            node.setOnMouseClicked(null);
        }
    }
}
