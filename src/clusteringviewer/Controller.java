package clusteringviewer;

import java.io.File;

/**
 * Created by Joachim on 04/11/2015.
 */
public class Controller {
    private View view;
    private Model model;

    public Controller(Model model) {
        this.model = model;
        this.view = new View();

        // Initialize Events
        this.setOpenFileEvents();
        this.setExitEvent();
    }

    public void show() {
        view.show(model.getStage());
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Events
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void setOpenFileEvents() {
        view.getOpen().setOnAction(value -> {
            File selectedFile = this.view.getFileChooser().showOpenDialog(
                    this.model.getStage());
            if (selectedFile != null) {
                this.model.setClsr(selectedFile);
                this.view.setClsrPath(selectedFile.getName(), selectedFile.getAbsolutePath());

                String absolutePath = selectedFile.getAbsolutePath();
                String path = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
                String clsrName = selectedFile.getName();
                String fastaName = clsrName.substring(0, clsrName.lastIndexOf("_"));

                File fastaFile = new File(path + File.separator + fastaName + ".fasta");
                if(fastaFile.exists() && !fastaFile.isDirectory()) {
                    this.model.setFasta(fastaFile);
                    this.view.setFastaPath(fastaFile.getName(), fastaFile.getAbsolutePath());
                } else
                    this.view.showDialog("Belonging fasta file was not" +
                            " found. Make sure the belonging fasta file" +
                            " is in the same directory as the clsr file.",
                            "File not found");

            }
        });
    }

    public void addTreeElements() {
        //this.view
    }

    public void setExitEvent() {
        view.getExit().setOnAction(value -> {
            this.model.getStage().close();
        });
    }
}
