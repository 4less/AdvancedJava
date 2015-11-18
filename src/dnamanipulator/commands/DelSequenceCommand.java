package dnamanipulator.commands;

import io.Sequence;
import javafx.collections.ObservableList;

/**
 * Created by Joachim on 13/11/2015.
 */
public class DelSequenceCommand implements Command {
    private ObservableList<Sequence> list;
    private Sequence sequence;
    private int index;

    public DelSequenceCommand(ObservableList<Sequence> list, int index) {
        this.list = list;
        this.index = index;
    }

    @Override
    public void execute() {
        this.sequence = list.get(index);
        this.list.remove(index);
    }

    @Override
    public void undo() {
        this.list.add(index, sequence);
    }
}
