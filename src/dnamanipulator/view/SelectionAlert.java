package dnamanipulator.view;

import io.NucleotideType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by Joachim on 18/11/2015.
 */
public class SelectionAlert extends Alert {
    private final ButtonType buttonTypeOne = new ButtonType("Filter&Keep");
    private final ButtonType buttonTypeTwo = new ButtonType("Discard");
    private final ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public SelectionAlert() {
        super(Alert.AlertType.CONFIRMATION);
        setTitle("Keep or Discard");
        setHeaderText("The current sequence was changed.");
        setContentText("Press \"Filter&Keep\" to filter and keep the sequence, \"Discard\" to discard the changes.");
        getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
    }

    public int getType() {
        Optional<ButtonType> result = showAndWait();
        if (result.get() == buttonTypeOne){
            return 1;
        } else if (result.get() == buttonTypeTwo) {
            return 2;
        } else {
            return 0;
        }
    }
}
