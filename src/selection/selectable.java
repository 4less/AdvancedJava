package selection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Created by Joachim on 07/02/2016.
 */
public interface Selectable {
    BooleanProperty selected = new SimpleBooleanProperty(false);

    default BooleanProperty selectedProperty() {
        return selected;
    }
}
