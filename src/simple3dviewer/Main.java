package simple3dviewer;

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
        View view = new View();
        view.show(primaryStage);
    }
}
