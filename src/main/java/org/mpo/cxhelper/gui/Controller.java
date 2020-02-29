package org.mpo.cxhelper.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mpo.cxhelper.service.TextProcessor;


import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Controller {
    Stage stage;
    public Button btn_findOccurences;
    public Button btn_loadFile;
    public Button btn_processHtmlToText;
    public Button btn_selectSymptome;
    //public TextArea text_areaFileShow;
    public TextArea text_areaOccurences;
    public TextArea text_areaHtmlShow;
    public TextArea text_areaSelectSymptomes;
    public TextField text_fieldFindRemedy;
    public TextField text_fieldFindSymptome;
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

    public void handle_text_field_key_pressFindRemedy(ActionEvent event) {
        Matcher matcher = TextProcessor.findString(text_fieldFindRemedy.getText(), text_areaOccurences.getText());
        if (matcher != null)
            text_areaOccurences.selectRange(matcher.start(), matcher.end());
    }
    public void handle_text_field_key_pressFindSymptome(ActionEvent event) {
        Matcher matcher = TextProcessor.findString(text_fieldFindSymptome.getText(), text_areaHtmlShow.getText());
        if (matcher != null)
            text_areaHtmlShow.selectRange(matcher.start(), matcher.end());
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
        /*text_areaFileShow.clear();
        if (file != null) {
            List<String> remedies = TextProcessor.preProcessText(file);
            remedies.forEach(m -> text_areaFileShow.appendText(m + "\n"));
        }*/
        if ((TextProcessor.remedies != null) && (!TextProcessor.remedies.isEmpty())) {
            btn_findOccurences.setVisible(true);
        }

    }

    public void btnp_loadAndProcessHtml(ActionEvent event) throws IOException {
        System.out.println("load HTML file to process!");
        File file = fileChooser.showOpenDialog(stage);
        text_areaHtmlShow.clear();
        if (file != null) {
            Map<String,List<String>> result = TextProcessor.processHtml(file);
            result.forEach((k,v) -> text_areaHtmlShow.appendText(k + " : " + v+"\n"));
        }
        TextProcessor.selectedSymptomesWithRemedies = new LinkedHashMap<>();


    }

    public void btnp_selectSymptome(ActionEvent event) throws IOException {
        System.out.println("Select symptome to process!");
        text_areaSelectSymptomes.clear();
        Map<String,List<String>> result = TextProcessor.selectSymptome(text_fieldFindSymptome.getText());
        result.forEach((k,v) -> text_areaSelectSymptomes.appendText(k + " : " + v+"\n"));

    }

    public void btnp_unSelectSymptome(ActionEvent event) throws IOException {
        System.out.println("Select symptome to process!");
        Map<String,List<String>> result = TextProcessor.unSelectSymptome(text_fieldFindSymptome.getText());
        text_areaSelectSymptomes.clear();
        result.forEach((k,v) -> text_areaSelectSymptomes.appendText(k + " : " + v+"\n"));

    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
