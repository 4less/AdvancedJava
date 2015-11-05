package clusteringviewer;

import io.FastA;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Joachim on 04/11/2015.
 */
public class View {
    private Scene scene;
    final private String title = "Clustering View Ultra Forte";

    private VBox root = new VBox();

    private MenuBar menuBar = new MenuBar();
    final Menu file = new Menu("File");
    private MenuItem open = new MenuItem("Open");
    private MenuItem exit = new MenuItem("Exit");
    private FileChooser fileChooser = new FileChooser();

    private HBox labelBox = new HBox();
    private Label fastaLabel = new Label("Fasta File: No file selected");
    private Label clsrLabel = new Label("Clsr File: No file selected");

    private TreeTableView treeTableView = new TreeTableView();
    private ArrayList<TreeItem> treeItems = new ArrayList<TreeItem>();

    public View() {
        this.setMenuBar();
        this.setFileChooser();
        this.setLabelBar();
        this.setTreeTableView();

        // add elements to root
        this.root.getChildren().addAll(this.menuBar, this.labelBox, this.treeTableView);

        // Init Scene
        this.scene = new Scene(root, 500, 500);
    }

    public void show(Stage stage) {
        stage.setTitle(this.title);
        stage.setScene(this.scene);
        stage.show();
    }

    public void setFileChooser() {
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Cluster Files", "*.clsr"));
    }

    public void setLabelBar() {
        this.labelBox.setSpacing(20);
        this.labelBox.setPadding(new Insets(2,15,2,15));
        this.labelBox.getChildren().addAll(fastaLabel, clsrLabel);
    }

    public void setMenuBar() {
        this.menuBar.getMenus().add(file);
        this.file.getItems().addAll(open, exit);
    }

    public void setTreeTableView() {
        this.treeTableView.setMaxHeight(Double.MAX_VALUE);
        TreeTableColumn<String, String> sequenceId =
                new TreeTableColumn<String, String>("SequenceId");
        TreeTableColumn<String, String> strain =
                new TreeTableColumn<String, String>("Strain");
        TreeTableColumn<String, String> sequenceLength =
                new TreeTableColumn<String, String>("Sequence Length");
        TreeTableColumn<String, String> sequenceSimilarity =
                new TreeTableColumn<String, String>("Sequence Similarity");

        sequenceId.setPrefWidth(100);
        strain.setPrefWidth(300);
        sequenceLength.setPrefWidth(50);
        sequenceSimilarity.setPrefWidth(50);

        this.treeTableView.getColumns().addAll(
                sequenceId, strain, sequenceLength, sequenceSimilarity);
    }

    public TreeTableView getTreeTableView() {
        return treeTableView;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public MenuItem getOpen() {
        return open;
    }

    public void setFastaPath(String fastaPath, String fastaFullPath) {
        this.fastaLabel.setText("Fasta file: " + fastaPath);
        this.fastaLabel.setTooltip(new Tooltip(fastaFullPath));
    }

    public void setClsrPath(String clsrPath, String clsrFullPath) {
        this.clsrLabel.setText("Clsr File: " + clsrPath);
        this.clsrLabel.setTooltip(new Tooltip(clsrFullPath));
    }

    public Label getClsrLabel() {
        return clsrLabel;
    }

    public Label getFastaLabel() {
        return fastaLabel;
    }

    public MenuItem getExit() {
        return exit;
    }

    public void showDialog(String dialogText, String dialogHeader) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(dialogHeader);
        VBox dialogBox = new VBox();
        dialogBox.setSpacing(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(15,15,15,15));
        Label dialogLabel = new Label(io.Sequence.toString(dialogText, 60));
        Button ok = new Button("ok");
        ok.setPrefWidth(70);
        ok.setOnAction(value -> {
            dialogStage.close();
        });
        dialogBox.getChildren().addAll(dialogLabel,ok);
        dialogStage.setScene(new Scene(dialogBox));
        dialogStage.show();
    }

    public void addTreeRoots(String cluster) {
        TreeItem<String> roots = new TreeItem<String>(cluster);
        //this.treeTableView.get
    }
}
