package dnamanipulator.commands;

/**
 * Created by Joachim on 11/11/2015.
 */
public interface Command {
    /**
     * execute the command
     */
    public abstract void execute();

    /**
     * undo the last command
     */
    public abstract void undo();
}
