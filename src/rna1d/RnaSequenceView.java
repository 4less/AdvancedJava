package rna1d;

import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jogi on 21.01.2016.
 */
public class RnaSequenceView {
    private final ArrayList<BaseBox> rnaSequence = new ArrayList<>();

    public void processSequence(String sequence) throws IOException {
        rnaSequence.clear();
        char[] charSeq = sequence.toCharArray();

        for (int i = 0; i < charSeq.length; i++) {
            rnaSequence.add(new BaseBox(BaseType.getBaseType(charSeq[i], i), i));
        }
    }

    public ArrayList<BaseBox> getRnaSequence() {
        return rnaSequence;
    }
}
