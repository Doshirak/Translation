import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser {
    private Lexer lexer;
    private ArrayList<Character> terminals;
    private ArrayList<Character> ExpressionTerminals;
    private ArrayList<Character> TermTerminals;
    private ArrayList<Character> FactorTerminals;
    private ArrayList<Character> PowerTerminals;
    private ArrayList<Character> AtomTerminals;

    public Parser(InputStreamReader reader) {
        lexer = new Lexer(reader);
        ExpressionTerminals = new ArrayList<Character>();
        TermTerminals = new ArrayList<Character>();
        FactorTerminals = new ArrayList<Character>();
        PowerTerminals = new ArrayList<Character>();
        AtomTerminals = new ArrayList<Character>();
        terminals = new ArrayList<Character>();
        ExpressionTerminals.add('+');
        ExpressionTerminals.add('-');
        TermTerminals.add('*');
        FactorTerminals.add('^');
        PowerTerminals.add('-');
        AtomTerminals.add('(');

        terminals.add('+');
        terminals.add('-');
        terminals.add('*');
        terminals.add('^');
        terminals.add('(');
        terminals.add(')');
    }

    public boolean isTerm() {
        return terminals.contains((char) lexer.read()) && !isAtomTerm();
    }

    public boolean isExpressionTerm() {
        return ExpressionTerminals.contains((char) lexer.read());
    }

    public boolean isTermTerm() {
        return TermTerminals.contains((char) lexer.read());
    }

    public boolean isFactorTerm() {
        return FactorTerminals.contains((char) lexer.read());
    }

    public boolean isPowerTerm() {
        return PowerTerminals.contains((char) lexer.read());
    }

    public boolean isAtomTerm() {
        return AtomTerminals.contains((char) lexer.read());
    }

    public boolean isAtom() {
        return lexer.isDigit() || isAtomTerm();
    }

    public int parse() throws ParserException {
        return parseExpression();
    }

    public int parseExpression() throws ParserException {
        int temp = 0;
        int sign = 1;
        boolean parse = true;

        while (parse) {
            if (isTerm()) {
                if (isExpressionTerm()) {
                    if ((char) lexer.read() == '+') {
                        sign = 1;
                    } else if ((char) lexer.read() == '-') {
                        sign = -1;
                    }
                    lexer.skip();
                } else if ((char) lexer.read() == ')') {
                    lexer.skip();
                    return temp;
                } else {
                    throw new ParserException();
                }
            } else if (lexer.read() < 0) {
                parse = false;
            } else if (isAtom()) {
                temp += sign * parseTerm();
            } else if ((char) lexer.read() == ')') {
                return temp;
            } else {
                throw new ParserException();
            }
        }
        return temp;
    }

    public int parseTerm() throws ParserException {
        int temp = 1;
        boolean parse = true;
        while (parse) {
            if (isTerm()) {
                if (isTermTerm()) {
                    lexer.skip();
                } else {
                    parse = false;
                }
            } else if (lexer.read() < 0) {
                parse = false;
            } else if (isAtom()) {
                temp *= parseFactor();
            } else {
                throw new ParserException();
            }
        }
        return temp;
    }

    public int parseFactor() throws ParserException {
        int temp = 0;
        int power = 1;
        boolean nextPower = false;
        boolean parse = true;
        while (parse) {
            if (isTerm()) {
                if (isFactorTerm()) {
                    nextPower = true;
                    lexer.skip();
                } else {
                    parse = false;
                }
            } else if (lexer.read() < 0) {
                parse = false;
            } else if (isAtom()) {
                if (nextPower) {
                    power = parseFactor();
                } else {
                    temp = parsePower();
                }
            } else {
                throw new ParserException();
            }
        }
        return (int) Math.pow(temp, power);
    }

    public int parsePower() throws ParserException {
        int temp = 0;
        int sign = 1;
        boolean setTemp = false;
        boolean parse = true;

        while (parse) {
            if (isTerm()) {
                if (isPowerTerm() && !setTemp) {
                    if ((char) lexer.read() == '-') {
                        sign = -1;
                    }
                    lexer.skip();
                } else {
                    parse = false;
                }
            } else if (lexer.read() < 0) {
                parse = false;
            } else if (isAtom()) {
                setTemp = true;
                temp = parseAtom();
            } else {
                throw new ParserException();
            }
        }
        return sign * temp;
    }

    public int parseAtom() throws ParserException {
        int temp = 0;
        if (isAtomTerm()) {
            if ((char) lexer.read() == '(') {
                lexer.skip();
                temp = parseExpression();
            }
        } else if (isTerm()) {
            throw new ParserException();
        } else {
            StringBuilder builder = new StringBuilder();
            do {
                builder.append((char) lexer.read());
                lexer.skip();
            } while (!isTerm() && (lexer.read() > 0));
            temp = Integer.parseInt(builder.toString());
        }
        return temp;
    }

    class ParserException extends Exception {
    }
}
