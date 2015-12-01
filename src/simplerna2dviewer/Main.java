package simplerna2dviewer;

import clusteringviewer.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Joachim on 18/11/2015.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewController viewController = new ViewController();
        viewController.show(primaryStage);
    }
}
