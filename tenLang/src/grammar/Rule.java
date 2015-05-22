package grammar;

import context.Context;
import lexer.Lexem;
import synanalizer.SynNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Rule {
    private boolean hasTerm = false;
    private boolean hasBraces = false;
    private ArrayList<Symbol> symbols = new ArrayList<Symbol>();
    private Map<Integer, Term> terms = new HashMap<Integer, Term>();
    private ArrayList<Term> termsList = new ArrayList<Term>();
    private HashMap<Integer, NotTerm> notTerms = new HashMap<Integer, NotTerm>();
    private int count = 0;

    public void add(String value, boolean special) {
        Term term = new Term(value, special);
        symbols.add(term);
        terms.put(count, term);
        termsList.add(term);
        count++;
        hasTerm = true;
    }

    public void add(NotTerm notTerm) {
        symbols.add(notTerm);
        notTerms.put(count, notTerm);
        count++;
    }

    public boolean withTerm() {
        return hasTerm;
    }

    public boolean parse(ArrayList<Lexem> lexems, ArrayList<SynNode> children, Context context) {
        ArrayList<Integer> delimiters = new ArrayList<Integer>();
        // find terminals positions
        delimiters.add(-1);
        if (hasBraces) {
            Term first = termsList.get(0);
            Term second = termsList.get(1);
            if (!first.parse(lexems, children, delimiters, context)) {
                return false;
            }
            if (!second.parseReverse(lexems, children, delimiters, context)) {
                return false;
            }
        } else {
            for (Term term : terms.values()) {
                if (!term.parse(lexems, children, delimiters, context)) {
                    return false;
                }
            }
        }
        delimiters.add(lexems.size());
        ArrayList<ArrayList<Lexem>> lArrays = new ArrayList<ArrayList<Lexem>>();
        // split lexems for notterminals with terminals positions
        for (int i = 1;i < delimiters.size();++i) {
            if (delimiters.get(i) - delimiters.get(i - 1) > 1) {
                lArrays.add(new ArrayList<Lexem>(lexems.subList(delimiters.get(i - 1) + 1, delimiters.get(i))));
            }
        }
        // more than one notterminals
        if (lArrays.size() > 1) {
            ArrayList<NotTerm> nArray = new ArrayList<NotTerm>(notTerms.values());
            for (int i = 0; i < nArray.size(); ++i) {
                if (!nArray.get(i).parse(lArrays.get(i), children, context)) {
                    return false;
                }
            }
        // one notterminal
        } else {
            for (NotTerm notTerm : notTerms.values()) {
                if (!notTerm.parse(lArrays.get(0), children, context)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Symbol symbol : symbols) {
            builder.append(symbol).append(" ");
        }
        return builder.toString();
    }

    public void setHasBraces(boolean hasBraces) {
        this.hasBraces = hasBraces;
    }
}
