package grammar;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Grammar {
    private Map<String, NotTerm> noTerms = new HashMap<String, NotTerm>();
    private NotTerm first;
    public Grammar() {
    }
    private NotTerm getNoTerm(String name) {
        NotTerm symbol = noTerms.get(name);
        if (symbol == null) {
            symbol = new NotTerm(name);
            noTerms.put(name, symbol);
        }
        if (first == null) {
            first = symbol;
        }
        return symbol;
    }
    private String trim(String string) {
        return string.substring(1, string.length() - 1);
    }

    public void read(String text) {
        Scanner scanner = new Scanner(text);
        read(scanner);
    }

    public void read(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        read(scanner);
    }

    public void read(Scanner scanner){
        ArrayList<String> strings = new ArrayList<String>();
        while (scanner.hasNext()) {
            String name = trim(scanner.next());
            NotTerm notTerm = getNoTerm(name);
            scanner.next();
            Rule currentRule = new Rule();
            String current;
            while (!(current = scanner.next()).equals(";") && scanner.hasNext()) {
                if (current.equals("|")) {
                    notTerm.addRule(currentRule);
                    currentRule = new Rule();
                    continue;
                }
                if (Pattern.matches("<\\w+>", current)) {
                    NotTerm ruleNoTerm = getNoTerm(trim(current));
                    currentRule.add(ruleNoTerm);
                } else if (Pattern.matches("\\[\\w+\\]", current)) {
                    currentRule.add(trim(current), true);
                } else if (Pattern.matches("\\{.+\\}", current)) {
                    currentRule.setHasBraces(true);
                    currentRule.add(trim(current), false);
                } else {
                    currentRule.add(trim(current), false);
                }
            }
            notTerm.addRule(currentRule);
        }
    }
    public void write() {
        for (NotTerm notTerm : noTerms.values()) {
            System.out.println(notTerm);
        }
    }
    public void write(String filename) {

    }
    public NotTerm getFirstNotTerm() {
        return first;
    }
}

