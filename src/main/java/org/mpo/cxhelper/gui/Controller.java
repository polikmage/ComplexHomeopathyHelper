package org.mpo.cxhelper.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mpo.cxhelper.service.TextProcessor;
import org.mpo.cxhelper.utils.IOUtils;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class Controller {
    Stage stage;
    public Button btn_findOccurences;
    public Button btn_loadFile;
    public TextArea text_areaFileShow;
    public TextArea text_areaOccurences;
    public TextField text_fieldFind;
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

    public void handle_text_field_key_pressFind(ActionEvent event) {
        Matcher matcher = TextProcessor.findString(text_fieldFind.getText(), text_areaOccurences.getText());
        if (matcher != null)
            text_areaOccurences.selectRange(matcher.start(), matcher.end());
    }

    public void btnp_findOccurences(ActionEvent event) {
        text_areaOccurences.clear();
        System.out.println("Button findOccurences was pressed!");
        Map<String, Integer> occurences = TextProcessor.findOccurences();
        occurences.entrySet().forEach(m -> text_areaOccurences.appendText(m.getKey() + " : " + m.getValue() + "\n"));
    }

    public void btnp_loadFile(ActionEvent event) throws IOException {
        System.out.println("load file!");
        File file = fileChooser.showOpenDialog(stage);
        text_areaFileShow.clear();
        if (file != null) {
            List<String> remedies = TextProcessor.preProcessText(file);
            remedies.forEach(m -> text_areaFileShow.appendText(m + "\n"));
        }
        if ((TextProcessor.remedies != null) && (!TextProcessor.remedies.isEmpty())) {
            btn_findOccurences.setVisible(true);
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
