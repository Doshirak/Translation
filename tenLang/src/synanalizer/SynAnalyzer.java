package synanalizer;

import errorhandler.ErrorHandler;
import grammar.Grammar;
import lexer.Lexem;
import lexer.Lexer;
import reader.Position;
import synnode.SynNode;

import java.io.*;
import java.util.ArrayList;

public class SynAnalyzer {
    private final Grammar grammar;
    private ErrorHandler errorHandler;
    private Position position;
    private ArrayList<Lexem> lexems;
    private Lexer lexer;
    private SynNode head;

    public SynAnalyzer(String s, ErrorHandler errorHandler, Position position, Grammar grammar) {
        this(new InputStreamReader(new ByteArrayInputStream(s.getBytes())), errorHandler, position, grammar);
    }

    public SynAnalyzer(InputStreamReader reader, ErrorHandler errorHandler, Position position, Grammar grammar) {
        this.errorHandler = errorHandler;
        this.position = position;
        this.grammar = grammar;
        lexer = new Lexer(reader, errorHandler, position);
    }

    public void read() {
        lexems = lexer.read();
        head = new SynNode(grammar.getFirstNotTerm(), errorHandler);
        head.read(lexems);
    }

    public void write(String filename) throws IOException {
//        OutputStream outputStream = new FileOutputStream(filename);
//        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
//        for (lexer.Lexem lexem : lexems) {
//            writer.write(lexem.toString() + "\n");
//        }
//        writer.close();
    }

    public void write() {
        writeNode(head, 0);
    }

    public void writeNode(SynNode node, int level) {
        for (int i = 0;i < level;++i) {
            System.out.print("-");
        }
        System.out.println(node.getNotTerm().getName());
        for (SynNode child : node.getChildren()) {
            writeNode(child, level + 1);
        }
    }
}
