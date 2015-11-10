package example;/**
 * Created by Joachim on 06/11/2015.
 */

import io.FastA;
import io.FastaReader;
import io.Sequence;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TreeTableViewExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        FastaReader fReader = null;
        try {
            fReader = new FastaReader("staph_aur_aur_16S.fasta");
        } catch (Exception e) {
            e.printStackTrace();
        }
        fReader.print();

        TreeItem<Sequence> rootItem = new TreeItem<Sequence>(new Sequence("", "root"));
        rootItem.setExpanded(true);
        TreeTableView treeTableView = new TreeTableView(rootItem);

        TreeTableColumn<Sequence, String> headerCol = new TreeTableColumn<>("Header");
        TreeTableColumn<Sequence, String> sequenceCol = new TreeTableColumn<>("Sequence");

        treeTableView.getColumns().setAll(headerCol, sequenceCol);

        headerCol.setCellValueFactory(p -> {
            return new ReadOnlyObjectWrapper(p.getValue().getValue().getHeader());
        });

        sequenceCol.setCellValueFactory(p -> {
            return new ReadOnlyObjectWrapper(p.getValue().getValue().toString());
        });


        root.getChildren().add(treeTableView);

        //Set scene
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DNA Manipulator 3000");
        primaryStage.show();
    }

  /*  private TreeItem<Sequence> createNode(final Sequence s) {
        final TreeItem<Sequence> node = new TreeItem<Sequence>(s) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<Sequence>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    Sequence f = (Sequence) getValue();
                    isLeaf = !f.isEmpty();
                }

                return isLeaf;
            }

        };
    }*/
}

