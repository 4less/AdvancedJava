package dnamanipulator.commands;

import io.Sequence;

/**
 * Created by Joachim on 14/11/2015.
 */
public class ChangeHeaderCommand implements Command {
    private String newHeader;
    private String oldHeader;
    private Sequence sequence;

    public ChangeHeaderCommand(Sequence sequence, String newHeader) {
        this.sequence = sequence;
        this.newHeader = newHeader;
        this.oldHeader = sequence.getHeader();
    }

    @Override
    public void execute() {
        this.sequence.setHeader(this.newHeader);
    }

    @Override
    public void undo() {
        this.sequence.setHeader(this.oldHeader);
    }
}
