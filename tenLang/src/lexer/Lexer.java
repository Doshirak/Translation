package lexer;

import context.Context;
import errorhandler.ErrorHandler;
import reader.Buffer;
import reader.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

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

    public Lexer(Context context) {
        this.buffer = new Buffer(context);
        this.position = context.getPosition();
        this.errorHandler = context.getErrorHandler();
        initTables();
    }

    public Lexer (InputStreamReader reader, ErrorHandler errorHandler, Position position) {
        this.errorHandler = errorHandler;
        this.position = position;
        buffer = new Buffer(reader, errorHandler, position);
        initTables();
    }

    private void initTables() {
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
        operators.add(")*");
        operators.add("/");
        operators.add("==");
        operators.add(":=");
        operators.add(":");
        operators.add(";");
        operators.add("U");
        operators.add(",");
        operators.add("#");
        operators.add("?");
        operators.add(">");
        operators.add("<");
        operators.add("=");
        operators.add(">=");
        operators.add("<=");
        operators.add(",");
        operators.add("(");
        operators.add(")");
        operators.add("{");
        operators.add("}");
        operators.add("]");
        operators.add("[");
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
        return Pattern.matches("\\w+", string);
    }

    private boolean isConstant(String string) {
        return Pattern.matches("\\d+", string);
    }

    private void trim() {
        while (!buffer.empty() && delimiters.contains((char) buffer.read())) {
            buffer.next();
        }
    }

    private boolean isComment() {
        if ((char) buffer.read() == '/' && (char) buffer.readPrev() == '/') {
            while (!buffer.empty() && (char) buffer.read() != '\n') {
                buffer.next();
            }
            buffer.next();
            return true;
        }
        return false;
    }

    public Lexem getToken() {
        Position tokenPosition = new Position(position);
        StringBuilder builder = new StringBuilder();
        trim();
        while (!buffer.empty() && !delimiters.contains((char) buffer.read())) {
            builder.append((char) buffer.read());
            buffer.next();
            if (isComment()) {
                return null;
            }
        }
        trim();
        String value = builder.toString();
        LexemType type = null;
        if (isOperator(value)) {
            type = LexemType.operator;
        } else if (isConstant(value)) {
            type = LexemType.constant;
        } else if (isVariable(value)) {
            type = LexemType.variable;
        } else {
            errorHandler.addError("wrong variable name", position);
        }
        return new Lexem(type, value, tokenPosition);
    }
}
