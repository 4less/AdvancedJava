package dnamanipulator;

import clusteringviewer.StageManager;
import dnamanipulator.commands.*;
import dnamanipulator.view.SelectionAlert;
import dnamanipulator.view.TypeAlert;
import dnamanipulator.view.View;
import io.FastaReader;
import io.NucleotideType;
import io.Sequence;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
/**
 * Created by Joachim on 11/11/2015.
 */
public class Presenter {
    private Model model;
    private View view;
    private final History history;

    public Presenter(Model model) {
        this.model = model;
        this.view = new View();

        // Init History
        this.history = new History();

        // Init Bindings
        bindButtonDisable();
        bindGcLength();

        // Initialize Events
        setOpenFileEvents();
        setSaveAsFileEvent();
        setSaveFileEvent();
        setOnExitEvent();
        setOnSelectionEvent();
        setOnNavigationButtonEvent();
        setOnProgramMenuItemEvent();
        setOnTextChangeEvent();
        setOnHeaderChangeEvent();
        setOnEditBoxButtonChangeEvent();

        show();
    }

    public void show() {
        view.show(StageManager.getInstance()
                .getPrimaryStage());
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Bindings
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void bindButtonDisable() {
        getView().getNavigation().disableButtonProperty(
                getModel().getFileLoadedProperty());
        getView().getEditBox().bindDisableButtonProperty(
                getModel().getFileLoadedProperty(), getModel().sequenceUpdatedProperty(),
                getView().getNavigation().getSelectionProperty());
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Events
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void setOpenFileEvents() {
        view.getProgramMenuBar().getOpen().setOnAction(value -> {
            history.clear();
            File selectedFile = this.view.getOpenFileChooser().showOpenDialog(
                    StageManager.getInstance().getPrimaryStage());
            if (selectedFile != null) {
                if (selectedFile.exists() && !selectedFile.isDirectory()) {
                    TypeAlert typeAlert = new TypeAlert();
                    NucleotideType type = typeAlert.getType();

                    if (type != null) {
                        try {
                            this.model.setFile(selectedFile, type);
                        } catch (Exception e) {
                            e.printStackTrace();
                            this.view.showDialog("File not Found",
                                    "Fasta file was not found.");
                        }

                        // Load ListView in NavigationBox
                        getView().getNavigation().setData(
                                model.getFastaReader().getSequences());

                        getView().getStatusBar().setSequenceCount(
                                Integer.toString(getModel()
                                        .getFastaReader().getLength()));
                        setType();
                        getModel().setSequenceUpdated(true);
                    }
                } else {
                    System.out.println("File does not exist or something else happened.");
                }
            }
        });
    }

    public void setSaveAsFileEvent() {
        getView().getProgramMenuBar().getSaveAs().setOnAction(value -> {
            File selectedFile = getView().getSaveFileChooser().showSaveDialog(
                    StageManager.getInstance().getPrimaryStage());
            try {
                getModel().setFile(selectedFile, getModel().getFastaReader().getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getModel().getFile()));
                getModel().getFastaReader().write(writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setSaveFileEvent() {
        getView().getProgramMenuBar().getSave().setOnAction(value -> {
            try {
                getModel().getFastaReader().write(new BufferedWriter(new FileWriter(getModel().getFile())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setOnSelectionEvent() {
        getView().getNavigation().getSequenceList().getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Sequence>() {
                    @Override
                    public void changed(ObservableValue<? extends Sequence> observable
                            , Sequence oldValue, Sequence newValue) {
                        if (newValue != null) getView().getNavigation().setIsSelected(true);
                        else getView().getNavigation().setIsSelected(false);

                        if (!getView().getNavigation().isSelfEvoke()) {
                            if (getModel().isFileLoaded() && newValue != null) {
                                if (oldValue != null && !getModel().isSequenceUpdated()) {
                                    SelectionAlert selectionAlert = new SelectionAlert();
                                    int choice = selectionAlert.getType();
                                    if (choice == 1) {
                                        history.execute(
                                                new TextChangeCommand(oldValue
                                                        , getView().getEditBox()
                                                        .getSequenceArea().getText(),
                                                        getView().getEditBox().getSequenceArea()));
                                        getModel().setSequenceUpdated(true);
                                    } else if (choice == 2){
                                        updateTextfield();
                                        getModel().setSequenceUpdated(true);
                                    } else {
                                        getView().getNavigation().setSelfEvoke(true);
                                        getView().getNavigation().getSequenceList().getSelectionModel().select(oldValue);
                                    }
                                }
                                if (getModel().isSequenceUpdated()) {
                                    updateTextfield();
                                    //eventuell ersetzen in model bei setsequence
                                    getModel().setSequenceUpdated(true);
                                }
                            }
                        }
                        getView().getNavigation().setSelfEvoke(false);
                    }
                });
    }

    public void setOnExitEvent() {
        getView().getProgramMenuBar().getExit().setOnAction(value ->
                StageManager.getInstance().getPrimaryStage().close());
    }

    public void setOnProgramMenuItemEvent() {
        getView().getProgramMenuBar().getRedo().setOnAction(value -> {
            Boolean updated = getModel().isSequenceUpdated();
            history.redo();
            updateTextfield();
            getModel().setSequenceUpdated(updated);

        });
        getView().getProgramMenuBar().getUndo().setOnAction(value -> {
            Boolean updated = getModel().isSequenceUpdated();
            history.undo();
            updateTextfield();
            getModel().setSequenceUpdated(updated);
        });
    }

    public void apply(Command cmd) {
        history.execute(cmd);
        updateTextfield();
        getModel().setSequenceUpdated(true);
    }

    public void setOnNavigationButtonEvent() {
        getView().getProgramMenuBar().getNewFile().setOnAction(value -> {
            TypeAlert typeAlert = new TypeAlert();
            NucleotideType type = typeAlert.getType();

            if (type != null) {
                getModel().setFastaReader(new FastaReader());
                getModel().getFastaReader().setType(type);
                try {
                    getModel().getFastaReader().add(new Sequence("", "New Sequence", type));
                    getModel().setSequenceUpdated(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    getModel().setFile(null, type);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load ListView in NavigationBox
                getView().getNavigation().setData(
                        model.getFastaReader().getSequences());
                // condense!
                getView().getStatusBar().setSequenceCount(
                        Integer.toString(getModel()
                                .getFastaReader().getLength()));
                setType();
            }
        });
        getView().getNavigation().getAdd().setOnAction(value -> {
            history.execute(new AddSequenceCommand(
                    getModel().getFastaReader().getSequences(), getModel().getFastaReader().getType()));
            getView().getStatusBar().setSequenceCount(Integer.toString(getModel().getFastaReader().getLength()));
        });
        getView().getNavigation().getDel().setOnAction(value -> {
            history.execute(new DelSequenceCommand(
                    getModel().getFastaReader().getSequences(),
                    getView().getNavigation().getSequenceList()
                            .getSelectionModel().getSelectedIndex()));
            getView().getStatusBar().setSequenceCount(Integer.toString(getModel().getFastaReader().getLength()));
        });
    }

    public void updateTextfield() {
        getView().getEditBox().getSequenceArea().setText(
                getSelectedItem().toString());
        getView().getEditBox().textWrap();
    }

    public IndexRange computeBeginEndIndexRangeNewline(IndexRange iR) {
        long lineLength = Math.round(getView().getEditBox().getSlider().getValue());
        IndexRange range = new IndexRange((int) (iR.getStart() - iR.getStart() / lineLength),
                (int) (iR.getEnd() - iR.getEnd() / lineLength));
        return range;
    }

    public void bindGcLength() {
        getView().setGcContentProperty(getModel().sequenceUpdatedProperty());
        getView().setLengthProperty(getModel().sequenceUpdatedProperty());
    }

    public Sequence getSelectedItem() {
        return getView().getNavigation().getSequenceList().getSelectionModel().getSelectedItem();
    }

    public void setOnEditBoxButtonChangeEvent() {
        getView().getEditBox().getConvert().setOnAction(value -> {
            apply(new ConvertCommand(getModel().getFastaReader()));
            setType();
        });
        getView().getEditBox().getFilter().setOnAction(value -> {
            apply(new TextChangeCommand(getSelectedItem()
                    , getView().getEditBox()
                    .getSequenceArea().getText(),
                    getView().getEditBox().getSequenceArea()));
        });
        getView().getEditBox().getLowerCase().setOnAction(value -> {
            IndexRange iR = computeBeginEndIndexRangeNewline(
                    getView().getEditBox().getSequenceArea().getSelection());
            apply(new LowerCaseCommand(
                    getSelectedItem(),
                    iR.getStart(),
                    iR.getEnd()));
        });
        getView().getEditBox().getUpperCase().setOnAction(value -> {
            IndexRange iR = computeBeginEndIndexRangeNewline(
                    getView().getEditBox().getSequenceArea().getSelection());
            apply(new UpperCaseCommand(
                    getSelectedItem(),
                    iR.getStart(),
                    iR.getEnd()));
        });
        getView().getEditBox().getReverse().setOnAction(value -> {
            apply(new ReverseCommand(getSelectedItem()));
        });
        getView().getEditBox().getComplementary().setOnAction(value -> {
            apply(new ComplementaryCommand(
                    getSelectedItem()));
        });
        getView().getEditBox().getReverseComplementary().setOnAction(value -> {
            apply(new ReverseComplementCommand(
                    getSelectedItem()));
        });
    }

    public void setOnTextChangeEvent() {
        getView().getEditBox().getSequenceArea().textProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {

                        if (getModel().isFileLoaded() && newValue != null &&
                                !newValue.replaceAll("\n", "").equals(oldValue)) {
                            getModel().setSequenceUpdated(false);
                            int length = newValue.replaceAll("\\n", "").length();
                            int sliderPos = (int) Math.round(getView().getEditBox().getSlider().getValue());
                            if (sliderPos >= length) sliderPos = 0;
                            if (sliderPos > 0 && length > 0 &&
                                    length % (int) getView().getEditBox()
                                            .getSlider().getValue() == 0) {
                                getView().getEditBox().getSequenceArea().setText(
                                        Sequence.toString(newValue.replaceAll(
                                                "\\n", ""), (int) getView()
                                                .getEditBox().getSlider().getValue()));
                            }
                        }
                    }
                });
    }

    public void setOnHeaderChangeEvent() {
        getView().getEditBox().getHeaderField().textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        new ChangeHeaderCommand(
                                getView().getNavigation().getSequenceList()
                                        .getSelectionModel().getSelectedItem(), newValue).execute();
                        getView().getNavigation().getSequenceList().refresh();
                    }
                });
    }

    public void setType() {
        getView().getStatusBar().setNucleoType(getModel().getFastaReader().getType().toString());
        String type;
        if (getModel().getFastaReader().getType() == NucleotideType.DNA) type = "RNA";
        else type = "DNA";
        getView().getEditBox().getConvert().setText("Convert to " + type);
    }

    public View getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }
}
