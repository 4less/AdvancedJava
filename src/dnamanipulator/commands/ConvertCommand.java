package dnamanipulator.commands;

import io.FastA;
import io.Nucleotide;
import io.NucleotideType;

/**
 * Created by Joachim on 15/11/2015.
 */
public class ConvertCommand implements Command {
    private FastA fasta;
    private NucleotideType type;

public ConvertCommand(FastA fasta) {
    this.fasta = fasta;
    this.type = fasta.getType();
}

    @Override
    public void execute() {
        if (type == NucleotideType.DNA) {
            try {
                fasta.convertTo(NucleotideType.RNA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == NucleotideType.RNA) {
            try {
                fasta.convertTo(NucleotideType.DNA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void undo() {
        try {
            fasta.convertTo(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
