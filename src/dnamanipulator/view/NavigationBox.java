package dnamanipulator.view;

import io.Sequence;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * Created by Joachim on 11/11/2015.
 */
public class NavigationBox extends VBox {
    private FlowPane commandButtons = new FlowPane();
    private Button add = new Button("add");
    private Button del = new Button("del");
    private Button selectAll = new Button("Select All");

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private boolean selfEvoke = false;

    private ListView<Sequence> sequenceList = new ListView<>();

    public NavigationBox() {
        super();
        this.setPrefWidth(200);

        initCommandButtons();

        this.getChildren().addAll(commandButtons, sequenceList);
    }

    public void setData(ObservableList<Sequence> sequenceList) {
        getSequenceList().setItems(sequenceList);
        getSequenceList().setCellFactory(list -> new FastaViewItem());
        getSequenceList().getSelectionModel().setSelectionMode(
                SelectionMode.SINGLE);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Bindings
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void disableButtonProperty(BooleanProperty isFileLoaded) {
        getDel().disableProperty().bind(isFileLoaded.and(getSelectionProperty()).not());
        getAdd().disableProperty().bind(isFileLoaded.not());
        getSelectAll().disableProperty().bind(isFileLoaded.not());
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Initializations
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void initCommandButtons() {
        getCommandButtons().getChildren().addAll(add,del);
        getCommandButtons().setPrefWidth(this.getWidth());
        getCommandButtons().setAlignment(Pos.CENTER);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Getter and Setter
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public FlowPane getCommandButtons() {
        return commandButtons;
    }

    public Button getAdd() {
        return add;
    }

    public Button getDel() {
        return del;
    }

    public Button getSelectAll() {
        return selectAll;
    }

    public ListView<Sequence> getSequenceList() {
        return sequenceList;
    }

    public BooleanProperty getSelectionProperty() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
    }

    public boolean isSelfEvoke() {
        return selfEvoke;
    }

    public void setSelfEvoke(boolean selfEvoke) {
        this.selfEvoke = selfEvoke;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Inner Class
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public class FastaViewItem extends ListCell<Sequence>{
        public FastaViewItem() {}

        @Override protected void updateItem(Sequence item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "" : item.getHeader());

            if (item != null) {
                setTextFill(isSelected() ? Color.BLACK :
                Color.BLACK);
            }
        }
    }
}
