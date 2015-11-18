package dnamanipulator.commands;

import io.Nucleotide;
import io.Sequence;

import java.util.Vector;

/**
 * Created by Joachim on 11/11/2015.
 */
public abstract class CaseCommand {
    private int begin;
    private int end;
    private String oldState;
    private Sequence newState;

    public CaseCommand(Sequence sequence) {
        this.newState = sequence;
        saveOldState();
    }

    public CaseCommand(Sequence sequence, int begin, int end) {
        this.newState = sequence;
        if (begin == end) {
            this.begin = 0;
            this.end = sequence.getLength();
        } else {
            this.begin = begin;
            this.end = end;
        }
        saveOldState();
    }

    public void saveOldState() {
        oldState = newState.toString();
        System.out.println(oldState);
    }

    public void undo() {
        newState.setSequence(oldState);
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public Sequence getNewState() {
        return newState;
    }
}
