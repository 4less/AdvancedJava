package clusteringviewer;/**
 * Created by Joachim on 04/11/2015.
 */

import javafx.application.Application;
import javafx.stage.Stage;

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
        Controller controller = new Controller(model);
        controller.show();
    }
}
