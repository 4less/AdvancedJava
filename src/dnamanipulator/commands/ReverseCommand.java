package dnamanipulator.commands;

import io.Sequence;

/**
 * Created by Joachim on 11/11/2015.
 */
public class ReverseCommand implements Command {
    private Sequence sequence;

    public ReverseCommand(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public void execute() {
        sequence.reverse();
    }

    @Override
    public void undo() {
        sequence.reverse();
    }
}
