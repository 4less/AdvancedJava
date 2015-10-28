package example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Demo program, Oct 21, 2015
 * Daniel Huson
 */
public class HelloWorld2 extends Application{
    @Override
    public void init() throws Exception {
        System.err.println("init()");
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.err.println("start()");

        FlowPane root=new FlowPane();
        root.setAlignment(Pos.CENTER);

        Label label=new Label("Hello World!");

        Button button=new Button("Bye!");

        button.setOnAction((value)-> Platform.exit());

        root.getChildren().addAll(label, button);

        Scene scene=new Scene(root,400,400);

        scene.setOnMouseClicked((me)->{
            int times=me.getClickCount();

            switch(me.getButton()) {
                case PRIMARY:
                    label.setText("primary "+times);
                    break;
                case SECONDARY:
                    label.setText("secondary "+times);
                    break;
                default:
                    label.setText("Other "+times);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello world");

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.err.println("stop()");
    }
}
