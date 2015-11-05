package clusteringviewer;

import io.ClsrReader;
import io.FastaReader;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Joachim on 04/11/2015.
 */
public class Model {
    private Stage stage;
    private File fasta;
    private File clsr;
    private FastaReader fReader;
    private ClsrReader cReader;

    public Model(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setFasta(File fasta) {
        this.fasta = fasta;
    }

    public void setClsr(File clsr) {
        this.clsr = clsr;
    }
}
