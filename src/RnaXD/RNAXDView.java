package rnaxd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jogi on 20.01.2016.
 */
public class RNAXDView  {
    private Parent root;

    @FXML
    private TextArea errorArea;

    public RNAXDView() throws IOException {
        root = FXMLLoader.load(getClass().getResource("project2_split.fxml"));
        loadFXMLObjects();
        test();
    }

    private void loadFXMLObjects() {
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void test() {
        errorArea.setText("Test is successfull!");
    }
}
