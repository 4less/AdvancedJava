package rnaxd;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.File;

/**
 * Created by Jogi on 20.01.2016.
 */
public class RNAXDModel {
    private BooleanProperty FILE_LOADED = new SimpleBooleanProperty(false);

    public boolean getFILE_LOADED() {
        return FILE_LOADED.get();
    }

    public void setFileLoaded(boolean isloaded) {
        FILE_LOADED.set(isloaded);
    }

    public ReadOnlyBooleanProperty fileLoadedProperty() {
        return FILE_LOADED;
    }
}
