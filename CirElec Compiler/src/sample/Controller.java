package sample;

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

public class Controller extends Application {
    private Stage stage;
    @FXML private Pane paneSote;

    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String sampleCode = String.join("\n", new String[] {
            "package com.example;",
            "",
            "import java.util.*;",
            "",
            "public class Foo extends Bar implements Baz {",
            "",
            "    /*",
            "     * multi-line comment",
            "     */",
            "    public static void main(String[] args) {",
            "        // single-line comment",
            "        for(String arg: args) {",
            "            if(arg.length() != 0)",
            "                System.out.println(arg);",
            "            else",
            "                System.err.println(\"Warning: empty string as argument\");",
            "        }",
            "    }",
            "",
            "}"
    });

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
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
