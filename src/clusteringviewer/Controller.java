package clusteringviewer;

import io.FastaReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Joachim on 04/11/2015.
 */
public class Controller {
    private View view;
    private Model model;

    public Controller(Model model) {
        this.model = model;
        this.view = new View();

        // Init Bindings
        this.bindFileName();

        // Initialize Events
        this.setOpenFileEvents();
        this.setExitEvent();
        //this.listenHeight(view.getScene());
    }

    public void bindFileName() {
        this.view.getClsrLabel().textProperty().bind(this.model.getClsrNameProperty());
        this.view.getFastaLabel().textProperty().bind(this.model.getFastaNameProperty());
        this.view.getClsrLabel().getTooltip().textProperty().bind(this.model.getClsrPathProperty());
        this.view.getFastaLabel().getTooltip().textProperty().bind(this.model.getFastaPathProperty());
        this.model.setClsrPath("Select clsr file", "No clsr file selected");
        this.model.setFastaPath("Select fasta file", "No fasta file selected");
    }

    public void show() {
        view.show(StageManager.getInstance().getPrimaryStage());
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Events
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void setOpenFileEvents() {
        view.getOpen().setOnAction(value -> {
            File selectedFile = this.view.getFileChooser().showOpenDialog(
                    StageManager.getInstance().getPrimaryStage());
            if (selectedFile != null) {
                String absolutePath = selectedFile.getAbsolutePath();
                String path = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
                String clsrName = selectedFile.getName()+ ".clsr";
                String fastaName = clsrName.substring(0, clsrName.lastIndexOf("_")) + ".fasta";

                this.model.setClsrPath(selectedFile.getPath(), clsrName);
                File fastaFile = new File(path + File.separator + fastaName);
                if(fastaFile.exists() && !fastaFile.isDirectory()) {
                    try {
                        this.model.setFastaPath(fastaFile.getPath(), fastaName);
                        this.model.readClsr(new FastaReader(new BufferedReader(new FileReader(fastaFile))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.view.setData(this.model.getcReader().getCluster());
                } else
                    this.view.showDialog("Belonging fasta file was not" +
                            " found. Make sure the belonging fasta file" +
                            " is in the same directory as the clsr file.",
                            "File not found");
            }
        });
    }

    public void setExitEvent() {
        view.getExit().setOnAction(value -> {
            StageManager.getInstance().getPrimaryStage().close();
        });
    }
}
