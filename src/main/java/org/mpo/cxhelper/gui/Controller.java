package org.mpo.cxhelper.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mpo.cxhelper.service.TextProcessor;
import org.mpo.cxhelper.utils.IOUtils;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Controller {
    Stage stage;
    public Button btn_findOccurences;
    public Button btn_loadFile;
    public TextArea text_areaFileShow;
    public TextArea text_areaOccurences;
    private FileChooser fileChooser = new FileChooser();
   // TextProcessor textProcessor;
    public Controller() {
        //set some system props - does not have access to fxml scene
        System.out.println("first");
        //textProcessor = new TextProcessor();
    }

    @FXML
    public void initialize() {
        //have access to fxml scene
        System.out.println("second");
        btn_findOccurences.setVisible(false);
    }

    public void btnp_findOccurences(ActionEvent event){
        System.out.println("Button findOccurences was pressed!");
        TextProcessor.findOccurences();
    }

    public void btnp_loadFile(ActionEvent event) throws IOException {
        System.out.println("load file!");
        File file = fileChooser.showOpenDialog(stage);
        text_areaFileShow.clear();
        if (file != null) {
            List<String> remedies =TextProcessor.preProcessText(file);
            remedies.forEach(m->text_areaFileShow.appendText(m+"\n"));
        }
        if(!TextProcessor.remedies.isEmpty()){
            btn_findOccurences.setVisible(true);
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
