package moleview;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Joachim on 09/12/2015.
 */
public class MoleViewMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        MoleViewView view = new MoleViewView();
        view.show(primaryStage);
    }
}
