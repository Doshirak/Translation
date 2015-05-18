package grammar;

import context.Context;
import lexer.Lexem;
import synanalizer.SynNode;

import java.util.ArrayList;

public class NotTerm implements Symbol {
    private String name;
    private ArrayList<Rule> termRules = new ArrayList<Rule>();
    private ArrayList<Rule> noTermRules = new ArrayList<Rule>();

    public NotTerm(String name) {
        this.name = name;
    }

    public boolean parse(ArrayList<Lexem> lexems, ArrayList<SynNode> children, Context context) {
        SynNode synNode = new SynNode(this, context, lexems.get(0).getPosition());
        for (Rule rule : termRules) {
            if (rule.parse(lexems, synNode.getChildren(), context)) {
                children.add(synNode);
                return true;
            }
        }
        for (Rule rule : noTermRules) {
            if (rule.parse(lexems, synNode.getChildren(), context)) {
                children.add(synNode);
                return true;
            }
        }
        return false;
    }

    public void addRule(Rule rule) {
        if (rule.withTerm()){
            termRules.add(rule);
        } else {
            noTermRules.add(rule);
        }
    }

    public ArrayList<Rule> getTermRules() {
        return termRules;
    }

    public ArrayList<Rule> getNoTermRules() {
        return noTermRules;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public boolean isTerm() {
        return false;
    }

    @Override
    public boolean isNoTerm() {
        return true;
    }

}
