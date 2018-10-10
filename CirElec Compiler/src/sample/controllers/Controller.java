package sample.controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.Constants.Configs.*;

public class Controller extends Application {
    private Stage stage;
    @FXML private Pane paneSote;



    CodeArea codeArea = new CodeArea();

    @FXML protected void initialize(){
        // add line numbers to the left of area
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.replaceText(0, 0, sampleCode);
        codeArea.setPrefSize(800,500);
        Subscription cleanupWhenNoLongerNeedIt = codeArea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        paneSote.getChildren().add(codeArea);
    }//llave initialize

    public void evtclose(ActionEvent event){
        System.exit(0);
    }

    public void evtopen(ActionEvent event){
        FileChooser of=new FileChooser();
        of.setTitle("Abrir archivo Food Compiler yea");
        FileChooser.ExtensionFilter filtro= new FileChooser.ExtensionFilter("Archivos .food","*.food");
        of.getExtensionFilters().add(filtro);
        File file=of.showOpenDialog(stage);
    }//llave abrir

    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
    }

}
