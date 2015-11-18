package dnamanipulator.commands;

import io.Sequence;
import javafx.scene.control.TextArea;

/**
 * Created by Joachim on 14/11/2015.
 */
public class TextChangeCommand implements Command {
    private String oldString;
    private String newString;
    private Sequence sequence;
    private TextArea area;

    public TextChangeCommand(Sequence sequence, String textAreaString, TextArea area) {
        this.sequence = sequence;
        this.oldString =  this.sequence.toString();
        this.newString = textAreaString;
        this.area = area;
    }

    @Override
    public void execute() {
        this.sequence.setSequence(this.newString);
    }

    @Override
    public void undo() {
        this.sequence.setSequence(this.oldString);
        System.out.println("oldString");
        this.area.setText(this.newString);
    }
}
