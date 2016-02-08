package rnaxd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;



public class RNAXDMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Call controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("project.fxml"));
        Parent root = loader.load();

        ((RNAXDPresenter) loader.getController()).show(root, primaryStage);
    }
}
