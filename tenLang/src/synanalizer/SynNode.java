package synanalizer;

import context.Context;
import grammar.NotTerm;
import grammar.Rule;
import grammar.Term;
import lexer.Lexem;
import reader.Position;

import java.util.ArrayList;

public class SynNode {
    private boolean isTerm;
    private NotTerm notTerm;
    private Context context;
    private Term term;
    private Position position;
    private ArrayList<SynNode> children = new ArrayList<SynNode>();

    public SynNode(SynNode node) {
        this.isTerm = node.isTerm;
        this.notTerm = node.notTerm;
        this.context = node.context;
        this.term = node.term;
        this.position = node.position;
        this.children = node.children;
    }

    public SynNode(NotTerm notTerm, Context context, Position position) {
        this.notTerm = notTerm;
        this.context = context;
        this.position = position;
        isTerm = false;
    }

    public SynNode(Term term, Context context, Position position) {
        this.term = term;
        this.context = context;
        this.position = position;
        isTerm = true;
    }

    public void read(ArrayList<Lexem> lexems) {
        for (Rule rule : notTerm.getTermRules()) {
            if (rule.parse(lexems, children, context)) {
                return;
            }
        }
        for (Rule rule : notTerm.getNoTermRules()) {
            if (rule.parse(lexems, children, context)) {
                return;
            }
        }
    }


    public ArrayList<SynNode> getChildren() {
        return children;
    }

    public String getValue() {
        if (isTerm) {
            return term.getValue();
        } else {
            return "<" + notTerm.getValue() + ">";
        }
    }

    public Position getPosition() {
        return position;
    }

    public boolean isTerm() { return isTerm; }

    public Term getTerm() {
        return term;
    }
}
