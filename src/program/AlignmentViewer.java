package program;
import io.*;
import commandline.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Joachim on 28/10/2015.
 */
public class AlignmentViewer extends Application {
    CommandLine commandLine;
    FastaReader fastaReader;

    public void addOptions() throws Exception {
        this.commandLine.addOption("i","input",1,true,Type.FILE,"Fasta file to read in.","..*\\.(fasta|fa)");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parameters params = getParameters();
        final List<String> param = params.getRaw();
        String[] args = new String[param.size()];
        for(int i = 0; i < param.size(); i++)
            args[i] = param.get(i);
        this.commandLine = new CommandLine();
        this.addOptions();
        this.commandLine.parseArgs(args);

        if(this.commandLine.isOption("i")) {
            String filePath = this.commandLine.getOption("i").getFile();
            this.fastaReader = new FastaReader(filePath);
        }
        System.err.println("start()");

        BorderPane root = new BorderPane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        VBox right = new VBox();
        VBox center = new VBox();

        Label label=new Label("Hello World!");

        Button select = new Button("Select all");
        Button clears = new Button("Clear Selection");
        Button apply = new Button("Apply");

        CheckBox headers = new CheckBox("Show Headers");
        CheckBox sequenc = new CheckBox("Show Sequences");
        CheckBox shownum = new CheckBox("Show Numbering");


        select.setOnAction((value)-> Platform.exit());

        Label content = new Label("Test");
        content.setFont(Font.font("Monospaced", 12));
        content.setText(fastaReader.print(20,60, null));

        top.getChildren().add(new Label("View Alignment:"));
        center.getChildren().add(content);
        right.getChildren().add(headers);
        right.getChildren().add(sequenc);
        right.getChildren().add(shownum);
        bottom.getChildren().add(select);
        bottom.getChildren().add(clears);
        bottom.getChildren().add(apply);

        root.setTop(top);
        root.setCenter(center);
        root.setRight(right);
        root.setBottom(bottom);

        Scene scene=new Scene(root,800,400);

        headers.setOnAction((event)-> {
            if(headers.isSelected())
                content.setText(fastaReader.print(20,60,null));
            if(!headers.isSelected())
                content.setText("");
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Alignment Viewer");

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
