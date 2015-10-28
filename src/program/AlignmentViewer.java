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

    /**
     * Function to add the commmandline options
     * @throws Exception
     */
    public void addOptions() throws Exception {
        this.commandLine.addOption("i","input",1,true,Type.FILE,"Fasta file to read in.","..*\\.(fasta|fa)");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Pass commandline parameters
        Parameters params = getParameters();
        final List<String> param = params.getRaw();
        String[] args = new String[param.size()];
        for(int i = 0; i < param.size(); i++)
            args[i] = param.get(i);
        this.commandLine = new CommandLine();
        this.addOptions();
        this.commandLine.parseArgs(args);

        // if there is an input filepath, load fasta file
        if(this.commandLine.isOption("i")) {
            String filePath = this.commandLine.getOption("i").getFile();
            this.fastaReader = new FastaReader(filePath);
        }
        System.err.println("start()");

        // create new root pane and boxes
        BorderPane root = new BorderPane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        VBox right = new VBox();
        VBox center = new VBox();

        //Buttons at the bottom
        Button select = new Button("Select all");
        Button clears = new Button("Clear Selection");
        Button apply = new Button("Apply");

        // Checkboxes at the right
        CheckBox headers = new CheckBox("Show Headers");
        CheckBox sequenc = new CheckBox("Show Sequences");
        CheckBox shownum = new CheckBox("Show Numbering");

        //Node containing the fasta-String
        Label content = new Label("Test");
        content.setFont(Font.font("Monospaced", 12));
        //initiate content label
        checkBoxEval(content,headers,sequenc,shownum);

        // select all, select all checkboxes
        select.setOnAction((event)-> {
            headers.setSelected(true);
            sequenc.setSelected(true);
            shownum.setSelected(true);
        });
        // clear all, deselect all checkboxes
        clears.setOnAction((event)-> {
            headers.setSelected(false);
            sequenc.setSelected(false);
            shownum.setSelected(false);
        });
        // if pressed, apply changes in checkboxsettings
        apply.setOnAction((event) -> this.checkBoxEval(content,headers,sequenc,shownum));


        // add buttons and checkboxes to their respective panes
        top.getChildren().add(new Label("View Alignment:"));
        center.getChildren().add(content);
        right.getChildren().add(headers);
        right.getChildren().add(sequenc);
        right.getChildren().add(shownum);
        bottom.getChildren().add(select);
        bottom.getChildren().add(clears);
        bottom.getChildren().add(apply);

        // add h and vboxes to root pane
        root.setTop(top);
        root.setCenter(center);
        root.setRight(right);
        root.setBottom(bottom);

        // create new scene with fixed size
        Scene scene=new Scene(root,800,400);

        // set selection events for checkboxes
        headers.setOnAction((event)-> {
            checkBoxEval(content,headers,sequenc,shownum);
        });
        sequenc.setOnAction((event)-> {
            checkBoxEval(content,headers,sequenc,shownum);
        });
        shownum.setOnAction((event)-> {
            checkBoxEval(content,headers,sequenc,shownum);
        });

        // add scene to stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Alignment Viewer");

        primaryStage.show();
    }

    /**
     * Evaluate what parts shall be displayed regarding some booleans
     * @param label
     * @param headers
     * @param sequenc
     * @param shownum
     */
    public void checkBoxEval(Label label, CheckBox headers, CheckBox sequenc, CheckBox shownum) {
        label.setText(fastaReader.print(20,60,null,headers.isSelected(),sequenc.isSelected(),shownum.isSelected()));
    }

    public static void main(String[] args) {
        // Pass arguments to application
        Application.launch(args);
    }
}
