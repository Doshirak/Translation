package grammar;

import context.Context;
import lexer.Lexem;
import lexer.LexemType;
import synanalizer.SynNode;

import java.util.ArrayList;

public class Rule {
    private boolean hasTerm = false;
    private ArrayList<Symbol> symbols = new ArrayList<Symbol>();
    private ArrayList<Term> termsList = new ArrayList<Term>();
    private ArrayList<NotTerm> notTermsList = new ArrayList<NotTerm>();

    public void add(String value, boolean special) {
        Term term = new Term(value, special);
        symbols.add(term);
        termsList.add(term);
        hasTerm = true;
    }

    public void add(NotTerm notTerm) {
        symbols.add(notTerm);
        notTermsList.add(notTerm);
    }

    public boolean withTerm() {
        return hasTerm;
    }

    public boolean parse(ArrayList<Lexem> lexems, ArrayList<SynNode> children, Context context) {
        if (notTermsList.size() == 0) {
            if (lexems.size() != 1 || termsList.size() != 1) {
                return false;
            }
            if (termsList.get(0).check(lexems.get(0))) {
                Term term = new Term(termsList.get(0));
                term.setValue(lexems.get(0).getValue());
                SynNode synNode = new SynNode(term, context, lexems.get(0).getPosition());
                children.add(synNode);
                return true;
            }
            return false;
        }
        if (termsList.size() > 0) {
            SynNode firstTermNode = null;
            SynNode secondTermNode = null;
            ArrayList<Integer> delimiters = new ArrayList<Integer>();
            delimiters.add(-1);
            Term firstTerm = termsList.get(0);
            for (int i = 0;i < lexems.size();++i) {
                Lexem lexem = lexems.get(i);
                if (firstTerm.check(lexem)) {
                    Lexem openBrace = new Lexem(LexemType.operator, "(", null);
                    if (lexems.contains(openBrace) &&
                            (firstTerm.getValue().equals(";") || firstTerm.getValue().equals("U"))) {
                        boolean wrongDelimeter = false;
                        int openCount = 0;
                        int closeCount = 0;
                        for (int j = 0;j < i;++j) {
                            if (lexems.get(j).equals(openBrace)) {
                                openCount++;
                            }
                            if (lexems.get(j).getValue().equals(")") ||
                                    lexems.get(j).getValue().equals(")*") ||
                                    lexems.get(j).getValue().equals(")?")) {
                                closeCount++;
                            }
                        }
                        if (openCount != closeCount) {
                            wrongDelimeter = true;
                        }
                        openCount = 0;
                        closeCount = 0;
                        for (int j = i + 1;j < lexems.size();++j) {
                            if (lexems.get(j).equals(openBrace)) {
                                openCount++;
                            }
                            if (lexems.get(j).getValue().equals(")") ||
                                    lexems.get(j).getValue().equals(")*") ||
                                    lexems.get(j).getValue().equals(")?")) {
                                closeCount++;
                            }
                        }
                        if (openCount != closeCount) {
                            wrongDelimeter = true;
                        }
                        if (wrongDelimeter) {
                            continue;
                        }
                    }
                    delimiters.add(i);
                    firstTermNode = new SynNode(firstTerm, context, lexem.getPosition());
                    break;
                }
            }
            if (firstTermNode == null) {
                return false;
            }
            if (termsList.size() > 1) {
                Term secondTerm = termsList.get(1);
                for (int i = lexems.size() - 1;i >= delimiters.get(1);--i) {
                    Lexem lexem = lexems.get(i);
                    if (secondTerm.check(lexem)) {
                        delimiters.add(i);
                        secondTermNode = new SynNode(secondTerm, context, lexem.getPosition());
                        break;
                    }
                }
                if (secondTermNode == null) {
                    return false;
                }
            }
            children.add(firstTermNode);
            if (secondTermNode != null) {
                children.add(secondTermNode);
            }
            delimiters.add(lexems.size());
            ArrayList<ArrayList<Lexem>> lexemArrays = new ArrayList<ArrayList<Lexem>>();
            for (int i = 1;i < delimiters.size();++i) {
                int prev = delimiters.get(i- 1);
                int current = delimiters.get(i);
                if (current - prev > 1) {
                    ArrayList<Lexem> subList = new ArrayList<Lexem>(lexems.subList(prev + 1, current));
                    lexemArrays.add(subList);
                }
            }
            if (lexemArrays.size() != notTermsList.size()) {
                return false;
            }
            for (int i = 0; i < lexemArrays.size();++i) {
                ArrayList<Lexem> lexemArray = lexemArrays.get(i);
                NotTerm notTerm = notTermsList.get(i);
                if (!notTerm.parse(lexemArray, children, context)) {
                    return false;
                }
            }
            return true;
        } else {
            for (NotTerm notTerm : notTermsList) {
                if (!notTerm.parse(lexems, children, context)) {
                    return false;
                }
            }
            return true;
        }
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
