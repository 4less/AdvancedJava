package dnamanipulator;

import io.FastaReader;
import io.NucleotideType;
import io.Sequence;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Joachim on 11/11/2015.
 */
public class Model {
    private File file;
    private FastaReader fastaReader;
    private final BooleanProperty fileLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty sequenceUpdated = new SimpleBooleanProperty(false);

    public Model() {}

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Getter and Setter
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void setSequenceUpdated(boolean sequenceUpdated) {
        this.sequenceUpdated.set(sequenceUpdated);
    }

    public boolean isSequenceUpdated() {
        return sequenceUpdated.get();
    }

    public BooleanProperty sequenceUpdatedProperty() {
        return sequenceUpdated;
    }

    public void setFile(File file, NucleotideType type) throws Exception {
        if (file == null)
            this.file = null;
        else {
            setFastaReader(new FastaReader(new BufferedReader(
                    new FileReader(file)), type));
            this.file = file;
        }
        this.fileLoaded.setValue(true);
    }

    public File getFile() {
        return file;
    }

    public void setFastaReader(FastaReader fastaReader) {
        this.fastaReader = fastaReader;
    }

    public FastaReader getFastaReader() {
        return fastaReader;
    }

    public boolean isFileLoaded() {
        return fileLoaded.get();
    }

    public BooleanProperty getFileLoadedProperty() {
        return fileLoaded;
    }
}
