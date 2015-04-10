package lexer;

import errorhandler.ErrorHandler;
import reader.Buffer;
import reader.Position;

import java.io.*;
import java.util.ArrayList;

public class Lexer {
    private Position position;
    private ErrorHandler errorHandler;
    private ArrayList<Lexem> lexems = new ArrayList<Lexem>();
    private Buffer buffer;
    private ArrayList<Character> delimiters = new ArrayList<Character>();
    private ArrayList<String> operators = new ArrayList<String>();

    public Lexer(String s, ErrorHandler errorHandler, Position position) {
        this(new InputStreamReader(new ByteArrayInputStream(s.getBytes())), errorHandler, position);
    }

    public Lexer (InputStreamReader reader, ErrorHandler errorHandler, Position position) {
        this.errorHandler = errorHandler;
        this.position = position;
        buffer = new Buffer(reader, errorHandler, position);
        delimiters.add(' ');
        delimiters.add('\n');
        delimiters.add('\r');
        operators.add("program:");
        operators.add("var");
        operators.add("int");
        operators.add("array");
        operators.add("of");
        operators.add("upd");
        operators.add("app");
        operators.add("print");
        operators.add("input");
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");
        operators.add("==");
        operators.add(":=");
        operators.add(":");
        operators.add(";");
        operators.add(",");
        operators.add("#");
        operators.add("?");
        operators.add(">");
        operators.add("<");
        operators.add("=");
        operators.add(">=");
        operators.add("<=");
        operators.add(",");
    }

    public ArrayList<Lexem> read() {
        while (!buffer.empty()) {
            Lexem lexem = getToken();
            if (lexem != null)
                lexems.add(lexem);
        }
        return lexems;
    }

    public void write(String filename) throws IOException {
        OutputStream outputStream = new FileOutputStream(filename);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        for (Lexem lexem : lexems) {
            writer.write(lexem.toString() + "\n");
        }
        writer.close();
    }

    public void write() {
        for (Lexem lexem : lexems) {
            System.out.println(lexem);
        }
    }

    private boolean isOperator(String string) {
        return operators.contains(string);
    }

    private boolean isVariable(String string) {
        return (!isOperator(string) && !(isConstant(string)));
    }

    private boolean isConstant(String string) {
        char chars[] = string.toCharArray();
        if (chars[0] != '-' && !Character.isDigit(chars[0])) {
            return false;
        }
        for (int i = 1;i < chars.length;++i) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }

    public Lexem getToken() {
        Position tokenPosition = new Position(position.getRow(), position.getCol() - 1);
        StringBuilder builder = new StringBuilder();
        while (!buffer.empty() && !delimiters.contains((char) buffer.read())) {
            builder.append((char)buffer.read());
            buffer.next();
            if ((char) buffer.read() == '/' && (char) buffer.readPrev() == '/') {
                while (!buffer.empty() && (char) buffer.read() != '\n') {
                    buffer.next();
                }
                buffer.next();
                return null;
            }
        }
        while (!buffer.empty() && delimiters.contains((char) buffer.read())) {
            buffer.next();
        }
        String value = builder.toString();
        LexemType type = null;
        if (isOperator(value)) {
            type = LexemType.operator;
        } else if (isConstant(value)) {
            type = LexemType.constant;
        } else if (isVariable(value)) {
            type = LexemType.variable;
        }
        return new Lexem(type, value, tokenPosition);
    }

    public char getChar() {
        char result = (char) buffer.read();
        buffer.next();
        return result;
    }

    public char lookAhead() {
        return (char) buffer.read();
    }

}
