package grammar;

import lexer.Lexem;
import synnode.SynNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rule {
    private boolean hasTerm = false;
    private ArrayList<Symbol> symbols = new ArrayList<Symbol>();
    private Map<Integer, Term> terms = new HashMap<Integer, Term>();
    private HashMap<Integer, NotTerm> notTerms = new HashMap<Integer, NotTerm>();
    private int count = 0;

    public void add(String value, boolean special) {
        Term term = new Term(value, special);
        symbols.add(term);
        terms.put(count, term);
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

    public boolean parse(ArrayList<Lexem> lexems, ArrayList<SynNode> children) {
        ArrayList<Integer> delimiters = new ArrayList<Integer>();
        delimiters.add(-1);
        for (Term term : terms.values()) {
            if (!term.parse(lexems, children, delimiters)) {
                return false;
            }
        }
        delimiters.add(lexems.size());
        ArrayList<ArrayList<Lexem>> lArrays = new ArrayList<ArrayList<Lexem>>();
        for (int i = 1;i < delimiters.size();++i) {
            if (delimiters.get(i) - delimiters.get(i - 1) > 1) {
                lArrays.add(new ArrayList<Lexem>(lexems.subList(delimiters.get(i - 1) + 1, delimiters.get(i))));
            }
        }
        if (lArrays.size() > 1) {
            ArrayList<NotTerm> nArray = new ArrayList<NotTerm>(notTerms.values());
            for (int i = 0; i < nArray.size(); ++i) {
                if (!nArray.get(i).parse(lArrays.get(i), children)) {
                    return false;
                }
            }
        } else {
            for (NotTerm notTerm : notTerms.values()) {
                if (!notTerm.parse(lexems, children)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Symbol symbol : symbols) {
            builder.append(symbol).append(" ");
        }
        return builder.toString();
    }
}
