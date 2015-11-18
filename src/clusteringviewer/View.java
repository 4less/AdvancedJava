package clusteringviewer;

import io.Cluster;
import io.ClusterSequence;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

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
    private TilePane labelBox = new TilePane();


    private Label fastaLabel = new Label("No Fasta file.");
    private Label clsrLabel = new Label("No Clsr file.");

    private StringProperty fastaPath = new SimpleStringProperty();
    private StringProperty clsrPath = new SimpleStringProperty();

    private TreeTableView<ClusterTreeItem> treeTableView = new TreeTableView<>();

    public View() {
        this.setMenuBar();
        this.setFileChooser();
        this.setLabelBar();
        this.setTreeTableView();

        // add elements to root
        this.root.getChildren().addAll(this.menuBar, this.labelBox, this.treeTableView);
        this.root.setFillWidth(true);

        // Init Scene
        this.scene = new Scene(root, 500, 500);
        this.treeTableView.setPrefHeight(this.scene.getHeight()-this.labelBox.getHeight()-this.menuBar.getHeight());

        //String css = "style.css";
        //scene.getStylesheets().add(css);
    }

    public void show(Stage stage) {
        stage.setTitle(this.title);
        stage.setScene(this.scene);
        stage.show();
    }

    public ObservableList<TreeTableColumn<ClusterTreeItem, ?>> getColumns() {
        return this.treeTableView.getColumns();
    }

    public void setFileChooser() {
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Cluster Files", "*.clsr"));
    }

    public void setLabelBar() {
        this.labelBox.setPrefWidth(Double.MAX_VALUE);
        this.labelBox.setAlignment(Pos.CENTER);
        this.labelBox.setHgap(50);
        this.labelBox.setPadding(new Insets(2, 15, 2, 15));
        this.labelBox.getChildren().addAll(fastaLabel, clsrLabel);
        this.fastaLabel.setTooltip(new Tooltip());
        this.clsrLabel.setTooltip(new Tooltip());
    }

    public void setMenuBar() {
        this.menuBar.getMenus().add(file);
        this.file.getItems().addAll(open, exit);
    }

    public void setTreeTableView() {
        TreeTableColumn<ClusterTreeItem, String> sequenceId =
                new TreeTableColumn<>("SequenceId");
        TreeTableColumn<ClusterTreeItem, String> strain =
                new TreeTableColumn<>("Strain");
        TreeTableColumn<ClusterTreeItem, Integer> sequenceLength =
                new TreeTableColumn<>("Sequence Length");
        TreeTableColumn<ClusterTreeItem, String> sequenceSimilarity =
                new TreeTableColumn<>("Sequence Similarity");

        sequenceId.setCellValueFactory(new TreeItemPropertyValueFactory<ClusterTreeItem, String>("sequenceId"));
        strain.setCellValueFactory(new TreeItemPropertyValueFactory<ClusterTreeItem, String>("sequence"));
        sequenceLength.setCellValueFactory(new TreeItemPropertyValueFactory<ClusterTreeItem, Integer>("length"));
        sequenceSimilarity.setCellValueFactory(new TreeItemPropertyValueFactory<ClusterTreeItem, String>("similarity"));

        this.treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        for (TreeTableColumn column : this.treeTableView.getColumns()) {
            if (column.getText().equals("Strain")) {
            }
        }

        sequenceId.setPrefWidth(100);
        strain.setPrefWidth(300);
        sequenceLength.setPrefWidth(50);
        sequenceSimilarity.setPrefWidth(50);

        this.treeTableView.getColumns().addAll(
                sequenceId, strain, sequenceLength, sequenceSimilarity);
        treeTableView.setShowRoot(false);
    }

    public void setData(Vector<Cluster> cVector) {
        TreeItem<ClusterTreeItem> rootItem = new TreeItem<>(new ClusterTreeItem(cVector));
        for(ClusterTreeItem cti : rootItem.getValue().children) {
            TreeItem<ClusterTreeItem> item = new TreeItem<>(cti);
            for(ClusterTreeItem cti2 : cti.children) {
                item.getChildren().add(new TreeItem<>(cti2));
            }
            rootItem.getChildren().add(item);
        }
        treeTableView.setRoot(rootItem);
        treeTableView.refresh();
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

    public Scene getScene() {
        return scene;
    }

    /**
     * Inner class to represent the cluster sequences as a tree
     * to fill the table
     */
    public class ClusterTreeItem {
        String sequenceId;
        String sequence;
        Integer length;
        String similarity;
        List<ClusterTreeItem> children;

        public ClusterTreeItem(Vector<Cluster> cVector) {
            children = cVector.stream().map(ClusterTreeItem::new).collect(Collectors.toList());
            sequenceId = "";
            sequence = "";
            length = Integer.MIN_VALUE;
            similarity = "";
        }

        public ClusterTreeItem(Cluster cluster) {
            children = cluster.getSequences().stream().map(ClusterTreeItem::new).collect(Collectors.toList());
            sequenceId = cluster.getRepresentative().getHeader();
            sequence = cluster.getRepresentative().getSequenceString();
            length = cluster.getRepresentative().getLength();
            similarity = cluster.getRepresentative().getSimilarity();
        }

        public ClusterTreeItem(ClusterSequence cSequence) {
            children = Collections.emptyList();
            sequenceId = cSequence.getHeader();
            sequence = cSequence.getSequenceString();
            similarity = cSequence.getSimilarity();
            length = cSequence.getLength();
        }

        public String getSequenceId() {
            return sequenceId;
        }

        public String getSequence() {
            return sequence;
        }

        public Integer getLength() {
            return length;
        }

        public String getSimilarity() {
            return similarity;
        }
    }
}
