package dnamanipulator.commands;

import io.Sequence;

/**
 * Created by Joachim on 11/11/2015.
 */
public class ReverseComplementCommand implements Command {
    private Sequence sequence;

    public ReverseComplementCommand(Sequence sequence) {
        this.sequence = sequence;
    }


    @Override
    public void execute() {
        new ReverseCommand(sequence).execute();
        new ComplementaryCommand(sequence).execute();
    }

    @Override
    public void undo() {
        execute();
    }
}
