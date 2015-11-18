package dnamanipulator.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;


/**
 * Created by Joachim on 11/11/2015.
 */
public class ProgramMenuBar extends MenuBar {
    private Menu file = new Menu("File");
    private MenuItem newFile = new MenuItem("New");
    private MenuItem open = new MenuItem("Open");
    private MenuItem save = new MenuItem("Save");
    private MenuItem saveAs = new MenuItem("Save as");
    private MenuItem exit = new MenuItem("Exit");


    private Menu edit = new Menu("Edit");
    private MenuItem undo = new MenuItem("Undo");
    private MenuItem redo = new MenuItem("Redo");

    public ProgramMenuBar() {
        super();
        this.getMenus().add(file);
        this.getMenus().add(edit);

        this.file.getItems().addAll(newFile, open, save, saveAs, exit);
        this.edit.getItems().addAll(undo,redo);

        this.undo.setAccelerator(new KeyCodeCombination(KeyCode.Z
                , KeyCombination.CONTROL_DOWN));
        this.redo.setAccelerator(new KeyCodeCombination(KeyCode.Y
                , KeyCombination.CONTROL_DOWN));

    }

    public MenuItem getOpen() {
        return open;
    }

    public MenuItem getSave() {
        return save;
    }

    public MenuItem getSaveAs() {
        return saveAs;
    }

    public MenuItem getExit() {
        return exit;
    }

    public MenuItem getUndo() {
        return undo;
    }

    public MenuItem getRedo() {
        return redo;
    }

    public MenuItem getNewFile() {
        return newFile;
    }
}
