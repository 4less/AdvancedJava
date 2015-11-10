package clusteringviewer;

import io.ClsrReader;
import io.FastaReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Joachim on 04/11/2015.
 */
public class Model {
    private StringProperty fastaPath = new SimpleStringProperty();
    private StringProperty clsrPath = new SimpleStringProperty();
    private StringProperty clsrName = new SimpleStringProperty();
    private StringProperty fastaName = new SimpleStringProperty();

    private ClsrReader cReader;

    public StringProperty getClsrPathProperty() {
        return clsrPath;
    }

    public StringProperty getFastaPathProperty() {
        return fastaPath;
    }

    public StringProperty getClsrNameProperty() {
        return clsrName;
    }

    public StringProperty getFastaNameProperty() {
        return fastaName;
    }

    public void setClsrPath(String clsrPath, String name) {
        this.clsrPath.set(clsrPath);
        this.clsrName.set(name);
    }

    public void setFastaPath(String fastaPath, String name) {
        this.fastaPath.set(fastaPath);
        this.fastaName.set(name);
    }

    public void readClsr(FastaReader fReader) {
        try {
            this.cReader = new ClsrReader();
            this.cReader.setfReader(fReader);
            this.cReader.read(new BufferedReader(new FileReader(new File(this.getClsrPathProperty().get()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClsrReader getcReader() {
        return cReader;
    }
}
