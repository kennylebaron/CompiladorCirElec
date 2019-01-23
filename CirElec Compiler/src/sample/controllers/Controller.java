package sample.controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.reactfx.Subscription;
import sample.Constants.Configs;
import sample.Logica.Interprete;

import java.io.File;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.Constants.Configs.*;

public class Controller extends Application {
    private Stage stage;
    @FXML private HBox paneSote;
    @FXML TextArea txtconsola;
    @FXML private VBox vbox;
    CodeArea codeArea = new CodeArea();

    @FXML protected void initialize(){
        // add line numbers to the left of area
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.replaceText(0, 0, sampleCode);
        codeArea.getStyleClass().add("editor");
        HBox.setHgrow(codeArea, Priority.ALWAYS);
        VBox.setVgrow(codeArea,Priority.ALWAYS);
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
        of.setTitle("Abrir archivo CirElec Compiler");
        FileChooser.ExtensionFilter filtro= new FileChooser.ExtensionFilter("Archivos .cec","*.cec");
        of.getExtensionFilters().add(filtro);
        File file=of.showOpenDialog(stage);
    }//llave abrir

    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
    }//llave start

    public void ejecutar (ActionEvent evt){
        compilar();
    }//llave ejecutar

    public void compilar(){
        txtconsola.setText("");
        long tinicial=System.currentTimeMillis();

        String text = codeArea.getText();
        String[] renglones=text.split("\\n");

        boolean correcto=true;
        for (int i = 0; i < renglones.length; i++) {
            boolean bandera=false;
            if (!renglones[i].trim().equals("")){
                for (int j = 0; j < Configs.EXPRESIONES.length && bandera==false; j++) {
                    Pattern patron =Pattern.compile(Configs.EXPRESIONES[j]);
                    Matcher matcher=patron.matcher(renglones[i]);
                    if (matcher.matches()){
                        bandera=true;
                }
                }//llave forj
                if (bandera==false){
                        txtconsola.setText(txtconsola.getText()+ "Syntax Error: CirElec:  ("+(i+1)+")"+" \n");
                        correcto=false;
                }
            }
        }//llave fori
        if (correcto){
            Interprete in = new Interprete(text,txtconsola);
        }
        long tfinal=System.currentTimeMillis()-tinicial;
        if (tfinal==1){
            txtconsola.setText(txtconsola.getText() + " \n" + "Process compiled in : " + tfinal + " millisecond");
        }else {
            txtconsola.setText(txtconsola.getText() + " \n" + "Process compiled in : " + tfinal + " milliseconds");
        }
    }//llave compilar
}
