package synanalizer;

import context.Context;
import errorhandler.ErrorHandler;
import grammar.Grammar;
import lexer.Lexem;
import lexer.Lexer;
import reader.Position;

import java.util.ArrayList;

public class SynAnalyzer {
    private Grammar grammar;
    private ErrorHandler errorHandler;
    private Position position;
    private ArrayList<Lexem> lexems;
    private Lexer lexer;
    private SynNode head;
    private Context context;

    public SynAnalyzer(Context context, Grammar grammar) {
        this.context = context;
        this.errorHandler = context.getErrorHandler();
        this.position = context.getPosition();
        this.grammar = grammar;
        lexer = new Lexer(context);
    }

    public void read() {
        lexems = lexer.read();
        head = new SynNode(grammar.getFirstNotTerm(), context, new Position(0, 0));
        head.read(lexems);
    }

    public void write() {
        writeNode(head, 0);
    }

    private void writeNode(SynNode node, int level) {
        for (int i = 0;i < level;++i) {
            System.out.print("-");
        }
        System.out.println(node.getValue());
        for (SynNode child : node.getChildren()) {
            writeNode(child, level + 1);
        }
    }

    public SynNode getHead() {
        return head;
    }
}
