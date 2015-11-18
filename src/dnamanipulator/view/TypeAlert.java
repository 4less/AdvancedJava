package dnamanipulator.view;

import io.NucleotideType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by Joachim on 18/11/2015.
 */
public class TypeAlert extends Alert {
    private final ButtonType buttonTypeOne = new ButtonType("RNA");
    private final ButtonType buttonTypeTwo = new ButtonType("DNA");
    private final ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public TypeAlert() {
        super(Alert.AlertType.CONFIRMATION);
        setTitle("DNA or RNA?");
        setHeaderText("Choose nucleotide type.");
        setContentText("Press \"RNA\" for RNA, \"DNA\" for DNA.");
        getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
    }

    public NucleotideType getType() {
        Optional<ButtonType> result = showAndWait();
        if (result.get() == buttonTypeOne){
            return NucleotideType.RNA;
        } else if (result.get() == buttonTypeTwo) {
            return NucleotideType.DNA;
        } else {
            return null;
        }
    }
}
