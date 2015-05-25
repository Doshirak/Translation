package grammar;

import lexer.Lexem;
import lexer.LexemType;
import java.util.regex.Pattern;

public class Term implements Symbol {
    private String value;
    private boolean isConst = false;
    private boolean isVar = false;
    private boolean isEmpty = false;

    public Term(String value, boolean special) {
        if (!special) {
            this.value = value;
        } else {
            if (value.equals("VARIABLE")) {
                isVar = true;
            } else if (value.equals("CONSTANT")) {
                isConst = true;
            } else if (value.equals("EMPTY")) {
                isEmpty = true;
            }
        }
    }

    public Term(Term term) {
        this.value = term.getValue();
        this.isEmpty = term.empty();
        this.isVar = term.variable();
        this.isConst = term.constant();
    }

    public boolean check(Lexem lexem) {
        if (isVar && lexem.getType() == LexemType.variable) {
            return Pattern.matches("\\w+", lexem.getValue());
        } else if (isConst && lexem.getType() == LexemType.constant) {
            return Pattern.matches("\\d", lexem.getValue());
        } else if (isEmpty) {
            return lexem.getValue().isEmpty();
        } else if (lexem.getType() == LexemType.operator) {
            return lexem.getValue().equals(value);
        }
        return false;
    }

    public boolean constant() {
        return isConst;
    }

    public boolean variable() {
        return isVar;
    }

    public boolean empty() {
        return isEmpty;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isTerm() {
        return true;
    }

    @Override
    public boolean isNoTerm() {
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty) {
            return "EMPTY";
        } else if (isVar) {
            return "VARIABLE";
        } else if (isConst) {
            return "CONSTANT";
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

