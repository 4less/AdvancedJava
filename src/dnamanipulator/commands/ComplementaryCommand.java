package dnamanipulator.commands;

import io.Sequence;

/**
 * Created by Joachim on 11/11/2015.
 */
public class ComplementaryCommand implements Command {
    private Sequence sequence;

    public ComplementaryCommand(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public void execute() {
        sequence.complement();
    }

    @Override
    public void undo() {
        sequence.complement();
    }
}
