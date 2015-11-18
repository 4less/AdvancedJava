package dnamanipulator;

import clusteringviewer.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Joachim on 11/11/2015.
 */

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StageManager.getInstance().setPrimaryStage(primaryStage);
        // initialize new model
        Model model = new Model();

        // Call controller
        Presenter presenter = new Presenter(model);
        presenter.show();
    }
}
