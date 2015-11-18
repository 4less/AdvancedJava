package dnamanipulator.commands;

import io.Sequence;

/**
 * Created by Joachim on 11/11/2015.
 */
public class UpperCaseCommand extends CaseCommand implements Command {
    public UpperCaseCommand(Sequence sequence, int begin, int end) {
        super(sequence, begin, end);
    }

    @Override
    public void execute() {
        getNewState().toUpperCase(getBegin(),getEnd());
    }
}
