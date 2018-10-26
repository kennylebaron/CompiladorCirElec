package sample.Constants;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Configs {
    public static final String Username="";
    public static final String[] KEYWORDS = new String[] {
            "connect","component","switch","default","if","while","create","new","add"
    };
    public static final String[] KEYWORDS2 = new String[] {
            "node","current","voltage","ohm","ohms","volt","hertz","cable","resistance","powersource","ground"
    };

    public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    public static final String KEYWORD2_PATTERN ="\\b(" + String.join("|", KEYWORDS2)+ ")\\b";
    public static final String PAREN_PATTERN = "\\(|\\)";
    public static final String BRACE_PATTERN = "\\{|\\}";
    public static final String BRACKET_PATTERN = "\\[|\\]";
    public static final String SEMICOLON_PATTERN = "\\;";
    public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    public static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<KEYWORD2>" + KEYWORD2_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public static final String sampleCode = String.join("\n", new String[] {
            "//Se crea un a resistencia y un fuente de poder",
            "create new resistance r1",
            "create new powersource ps1",
            "value for r1 = 100 ohm",
            "value for ps1 = 6 volt",
            "connect r1 to ps1",
            "add new ground g1",
            "connect g1 to r1",
            "if (r1 connect ps1)-->print(\"Execute process successful\")",
            ""

    });
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("KEYWORD2") != null ? "keyword2" :
                                    matcher.group("PAREN") != null ? "paren" :
                                            matcher.group("BRACE")     != null ? "brace" :
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
    public static String[] EXPRESIONES={
            "\\s*create\\s+new\\s+resistance\\s+\\w+\\s*",
            "\\s*create\\s+new\\s+powersource\\s+\\w+\\s*",
            "\\s*connect\\s+\\w+\\s+to\\s+\\w+\\s*",
            "\\s*value\\s+for\\s+\\w+\\s*=\\s*\\d+\\s+ohm\\s*",
            "\\s*value\\s+for\\s+\\w+\\s*=\\s*\\d+ volt\\s*",
            "\\s*add\\s+new\\s+ground\\s+\\w+\\s*",
            "\\s*//(.+.)?\\s*",                                      //
            "if\\s*[(]\\s*(.+.)?\\s*[)]\\s*-->\\s*print\\s*[(]\\s*[\"](.+.)?[\"]\\s*[)]\\s*|if\\s*[(]\\s*(.+.)?\\s*[)]\\s*-->\\s*print\\s*[(]\\s*[\"](.+.)?[\"]\\s*(.+.)\\s*[)]\\s*",
    };
}