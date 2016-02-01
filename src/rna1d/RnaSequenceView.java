package rna1d;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.ListChangeListener;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.HBox;
import molecules.DrawModel;
import molecules.Nucleotide;
import molecules.NucleotideSelectionModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jogi on 21.01.2016.
 */
public class RnaSequenceView {
    private final ArrayList<BaseBox> rnaSequence = new ArrayList<>();
    private NucleotideSelectionModel<Nucleotide> selectionModel;

    public void processSequence(String sequence) throws IOException {
        rnaSequence.clear();
        char[] charSeq = sequence.toCharArray();

        for (int i = 0; i < charSeq.length; i++) {
            rnaSequence.add(new BaseBox(BaseType.getBaseType(charSeq[i], i), i));
        }
    }

    public void setSelectionModel(NucleotideSelectionModel model) {
        this.selectionModel = model;
        setSelectionCapture();
        setBoxSelection();
    }

    public void setBoxSelection() {
        for(int i=0;i< rnaSequence.size();i++) {
            final int index=i;

            rnaSequence.get(index).selectedProperty().bind(new BooleanBinding(){
                {
                    bind(selectionModel.getSelectedItems());
                }

                @Override
                protected boolean computeValue() {
                    return selectionModel.getSelectedIndices().contains(index);
                }
            });

        }
    }

    private void setSelectionCapture() {
        for (int i = 0; i < rnaSequence.size(); i++) {
            final int index = i;
            rnaSequence.get(index).setOnMouseClicked((me) -> {
                if (me.isControlDown()) {
                    System.out.println("control is down");
                    if (selectionModel.isSelected(index))
                        selectionModel.clearSelection(index);
                    else
                        selectionModel.select(index);
                } else if (selectionModel.isSelected(index)) {
                    selectionModel.clearSelection(index);
                } else {
                    selectionModel.clearSelection();
                    selectionModel.select(index);
                }
            });
        }
    }

    public ArrayList<BaseBox> getRnaSequence() {
        return rnaSequence;
    }
}
