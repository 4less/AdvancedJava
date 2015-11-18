package dnamanipulator.commands;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

import java.util.Stack;

/**
 * Created by Joachim on 11/11/2015.
 */
public final class History {
    private final Stack<Command> undoStack = new Stack<Command>();
    private final Stack<Command> redoStack = new Stack<Command>();

    public void execute(final Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        System.out.println("Execute: " + cmd.getClass().toString());
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            System.out.println("Command Undo: " + cmd.getClass().toString());
            redoStack.push(cmd);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }

    public void clear() {
        redoStack.clear();
        undoStack.clear();
    }

    public History() {
    }
}
