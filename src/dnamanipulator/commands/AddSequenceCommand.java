package dnamanipulator.commands;

import io.Nucleotide;
import io.NucleotideType;
import io.Sequence;
import javafx.collections.ObservableList;

/**
 * Created by Joachim on 13/11/2015.
 */
public class AddSequenceCommand implements Command {
    private ObservableList<Sequence> list;
    private NucleotideType nucleotideType;

    public AddSequenceCommand(ObservableList<Sequence> list, NucleotideType nucleotideType) {
        this.list = list;
        this.nucleotideType = nucleotideType;
    }

    @Override
    public void execute() {
        try {
            list.add(new Sequence("","New Sequence", nucleotideType));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        list.remove(list.size()-1);
    }
}
